package com.api.bank.service;

import com.api.bank.domain.customer.CustomerEntity;
import com.api.bank.domain.wallet.*;
import com.api.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final CustomerRepository customerRepository;
    private final WalletMapper walletMapper;

    public WalletModel getWallet(Long customerId) {
        var customer = findCustomer(customerId);
        return walletMapper.toModel(customer.getWallet());
    }

    public WalletModel recharge(Long customerId, RechargeRequest rechargeRequest) {
        var customer = findCustomer(customerId);
        recharge(customer, rechargeRequest.value());
        var customerAfterRecharge = customerRepository.save(customer);
        return walletMapper.toModel(customerAfterRecharge.getWallet());
    }

    public void transfer(Long customerIdThatWillTransfer, TransferRequest transferRequest) {
        var customerThatWillTransfer = findCustomer(customerIdThatWillTransfer);
        validateIfCustomerHasBalanceEnough(customerThatWillTransfer, transferRequest.value());
        var customerThatWillReceive = findCustomer(transferRequest.destinyCustomerId());

        debit(customerThatWillTransfer, transferRequest.value());
        recharge(customerThatWillReceive, transferRequest.value());

        customerRepository.save(customerThatWillTransfer);
        customerRepository.save(customerThatWillReceive);
    }

    private CustomerEntity findCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ObjectNotFoundException(WalletEntity.class, WalletEntity.class.getName()));
    }

    private void recharge(CustomerEntity customer, BigDecimal rechargeValue) {
        customer.getWallet().setBalance(customer.getWallet().getBalance().add(rechargeValue));
    }

    private void debit(CustomerEntity customer, BigDecimal valueToDebit) {
        customer.getWallet().setBalance(customer.getWallet().getBalance().subtract(valueToDebit));
    }

    private void validateIfCustomerHasBalanceEnough(CustomerEntity customer, BigDecimal valueToDebit) {
        var comparisonResult = customer.getWallet().getBalance().compareTo(valueToDebit);
        if (comparisonResult < 0)
            throw new IllegalArgumentException("Não há saldo suficiente");
    }
}
