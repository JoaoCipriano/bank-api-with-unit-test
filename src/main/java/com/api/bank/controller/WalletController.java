package com.api.bank.controller;

import com.api.bank.domain.wallet.RechargeRequest;
import com.api.bank.domain.wallet.TransferRequest;
import com.api.bank.domain.wallet.WalletModel;
import com.api.bank.domain.wallet.WithdrawRequest;
import com.api.bank.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/internet-banking")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/wallets")
    public ResponseEntity<WalletModel> getWallet(@RequestHeader("customerId") Long customerId) {
        return ResponseEntity.ok(walletService.getWallet(customerId));
    }

    @PatchMapping("wallets/recharge")
    public ResponseEntity<WalletModel> recharge(@RequestHeader("customerId") Long customerId, @Valid @RequestBody RechargeRequest rechargeRequest) {
        return ResponseEntity.ok(walletService.recharge(customerId, rechargeRequest));
    }

    @PatchMapping("wallets/withdraw")
    public ResponseEntity<String>  withdraw(@RequestHeader("customerId") Long customerId, @Valid @RequestBody WithdrawRequest withdrawRequest) {
        return ResponseEntity.ok("AnyString");
    }

    @PatchMapping("wallets/transfer")
    public ResponseEntity<String> transfer(@RequestHeader("customerId") Long customerId, @Valid @RequestBody TransferRequest transferRequest) {
        walletService.transfer(customerId, transferRequest);
        return ResponseEntity.ok("Transferencia concluida com sucesso");
    }
}
