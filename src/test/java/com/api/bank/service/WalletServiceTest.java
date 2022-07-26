package com.api.bank.service;

import com.api.bank.domain.customer.CustomerEntity;
import com.api.bank.domain.wallet.*;
import com.api.bank.repository.CustomerRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    private WalletService walletService;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private WalletMapper walletMapper;

    @BeforeEach
    void setUp() {
        walletService = new WalletService(customerRepository, walletMapper);
    }

    @Test
    void shouldGetCustomerWallet() {
        // ARRANGE (organizar)
        var customerId = 1L;
        var walletEntity = WalletEntity.builder().balance(BigDecimal.TEN).build();
        var customerEntity = CustomerEntity.builder().name("John Wick").wallet(walletEntity).build();
        var expectedWalletModel = WalletModel.builder().balance(BigDecimal.TEN).build();

        when(customerRepository.findById(customerId)).thenReturn(of(customerEntity));
        when(walletMapper.toModel(walletEntity)).thenReturn(expectedWalletModel);

        // ACT (agir)
        var actualWallet = walletService.getWallet(customerId);

        // ASSERT (verificar)
        assertThat(actualWallet).isEqualTo(expectedWalletModel);
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenGetWalletThatNotExist() {
        var customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(empty());

        assertThatThrownBy(() -> walletService.getWallet(customerId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No row with the given identifier exists");
    }

    @Test
    void shouldRechargeTheWallet() {
        var customerId = 1L;
        var rechargeRequest = RechargeRequest.builder().value(BigDecimal.TEN).build();

        var walletEntityBeforeRecharge = WalletEntity.builder().balance(BigDecimal.TEN).build();
        var walletEntityAfterRecharge = WalletEntity.builder().balance(BigDecimal.valueOf(20L)).build();
        var customerEntityBeforeRecharge = CustomerEntity.builder().name("John Wick").wallet(walletEntityBeforeRecharge).build();
        var customerEntityAfterRecharge = CustomerEntity.builder().name("John Wick").wallet(walletEntityAfterRecharge).build();

        var capturedCustomerEntity = ArgumentCaptor.forClass(CustomerEntity.class);

        var expectedWalletModel = WalletModel.builder().balance(BigDecimal.valueOf(20L)).build();

        when(customerRepository.findById(customerId)).thenReturn(of(customerEntityBeforeRecharge));
        when(customerRepository.save(capturedCustomerEntity.capture())).thenReturn(customerEntityAfterRecharge);
        when(walletMapper.toModel(walletEntityAfterRecharge)).thenReturn(expectedWalletModel);

        var actualWalletModel = walletService.recharge(customerId, rechargeRequest);
        var capturedWalletEntity = capturedCustomerEntity.getValue().getWallet();

        assertThat(actualWalletModel).isEqualTo(expectedWalletModel);
        assertThat(actualWalletModel).usingRecursiveComparison().isEqualTo(capturedWalletEntity);
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenRechargeAWalletThatNotExist() {
        var customerId = 1L;
        var rechargeRequest = RechargeRequest.builder().value(BigDecimal.TEN).build();

        when(customerRepository.findById(customerId)).thenReturn(empty());

        assertThatThrownBy(() -> walletService.recharge(customerId, rechargeRequest))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No row with the given identifier exists");
    }

    @Test
    void shouldTransferSuccessfullyWhenCustomerHasBalanceEnough() {
        var customerIdThatWillTransfer = 1L;
        var customerIdThatWillReceive = 2L;
        var transferRequest = TransferRequest.builder().value(BigDecimal.TEN).destinyCustomerId(customerIdThatWillReceive).build();
        var walletEntityToDebit = WalletEntity.builder().balance(BigDecimal.TEN).build();
        var walletEntityToCredit = WalletEntity.builder().balance(BigDecimal.TEN).build();
        var customerEntityToTransfer = CustomerEntity.builder().name("John Wick").wallet(walletEntityToDebit).build();
        var customerEntityToReceive = CustomerEntity.builder().name("Lara Croft").wallet(walletEntityToCredit).build();

        when(customerRepository.findById(customerIdThatWillTransfer)).thenReturn(of(customerEntityToTransfer));
        when(customerRepository.findById(customerIdThatWillReceive)).thenReturn(of(customerEntityToReceive));

        walletService.transfer(customerIdThatWillTransfer, transferRequest);

        assertThat(customerEntityToTransfer.getWallet().getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(customerEntityToReceive.getWallet().getBalance()).isEqualTo(BigDecimal.valueOf(20L));
    }

    @Test
    void shouldThrowExceptionWhenCustomerHasNotBalanceEnough() {
        var customerIdThatWillTransfer = 1L;
        var customerIdThatWillReceive = 2L;
        var transferRequest = TransferRequest.builder().value(BigDecimal.valueOf(11L)).destinyCustomerId(customerIdThatWillReceive).build();
        var walletEntityToDebit = WalletEntity.builder().balance(BigDecimal.TEN).build();
        var customerEntityToTransfer = CustomerEntity.builder().name("John Wick").wallet(walletEntityToDebit).build();

        when(customerRepository.findById(customerIdThatWillTransfer)).thenReturn(of(customerEntityToTransfer));

        assertThatThrownBy(() -> walletService.transfer(customerIdThatWillTransfer, transferRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Não há saldo suficiente");
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenTryTransferFromAWalletThatDoesNotExist() {
        var customerIdThatWillTransfer = 1L;
        var customerIdThatWillReceive = 2L;
        var transferRequest = TransferRequest.builder().value(BigDecimal.TEN).destinyCustomerId(customerIdThatWillReceive).build();

        when(customerRepository.findById(customerIdThatWillTransfer)).thenReturn(empty());

        assertThatThrownBy(() -> walletService.transfer(customerIdThatWillTransfer, transferRequest))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No row with the given identifier exists");
    }
}