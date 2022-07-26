package com.api.bank.controller;

import com.api.bank.domain.wallet.RechargeRequest;
import com.api.bank.domain.wallet.TransferRequest;
import com.api.bank.domain.wallet.WalletModel;
import com.api.bank.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    private WalletController walletController;

    @Mock
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletController = new WalletController(walletService);
    }

    @Test
    void shouldGetCustomerWallet_WithJUnitAssertions() {
        // ARRANGE (organizar)
        var customerId = 1L;
        var expectedWalletModel = WalletModel.builder().balance(BigDecimal.TEN).build();

        when(walletService.getWallet(customerId)).thenReturn(expectedWalletModel);

        // ACT (agir)
        var response = walletController.getWallet(customerId);

        // ASSERT (verificar)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedWalletModel, response.getBody());
    }

    @Test
    void shouldGetCustomerWallet_WithFluentAssertion() {
        var customerId = 1L;
        var expectedWalletModel = WalletModel.builder().balance(BigDecimal.TEN).build();

        when(walletService.getWallet(customerId)).thenReturn(expectedWalletModel);

        var response = walletController.getWallet(customerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedWalletModel);
    }

    @Test
    void shouldRechargeTheWallet() {
        var customerId = 1L;
        var rechargeRequest = RechargeRequest.builder().value(BigDecimal.TEN).build();
        var expectedWalletModel = WalletModel.builder().balance(BigDecimal.valueOf(20L)).build();

        when(walletService.recharge(customerId, rechargeRequest)).thenReturn(expectedWalletModel);

        var response = walletController.recharge(customerId, rechargeRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedWalletModel);
    }

    @Test
    void shouldTransferToAnotherWalletSuccessfully() {
        var customerIdThatWillTransfer = 1L;
        var customerIdThatWillReceive = 2L;
        var transferRequest = TransferRequest.builder().value(BigDecimal.TEN).destinyCustomerId(customerIdThatWillReceive).build();

        var response = walletController.transfer(customerIdThatWillTransfer, transferRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Transferencia concluida com sucesso");
        verify(walletService, atLeastOnce()).transfer(customerIdThatWillTransfer, transferRequest);
    }
}