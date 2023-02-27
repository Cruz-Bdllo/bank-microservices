package com.bank.account.controller;

import com.bank.account.dto.AccountResponse;
import com.bank.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/v0")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service; // Injected

    @GetMapping("/account/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse getAccount(@PathVariable int customerId) {
        return service.getAccountByCustomerId(customerId);
    }

    @GetMapping("/account")
    public ResponseEntity<?> getAccountList() {
        List<AccountResponse> accounts = service.getAllAccounts();
        return ResponseEntity.status((accounts.size() > 0)
                ? HttpStatus.OK
                : HttpStatus.NO_CONTENT).body(accounts);
    }
}
