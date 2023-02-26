package com.bank.account.service;

import com.bank.account.dto.AccountRequest;
import com.bank.account.dto.AccountResponse;
import com.bank.account.model.Account;
import com.bank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository; // injected

    public AccountResponse getAccountByCustomerId(String customerId) {
        Account accountFunded = repository.findAccountByCustomerId(customerId)
                .orElse(null);

        return (accountFunded != null) ? mapToResponse(accountFunded) : null;
    }

    public List<AccountResponse> getAllAccounts() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public String saveAccount(AccountRequest accountRequest) {
        boolean existAccount = repository
                .findAccountByCustomerId(accountRequest.getCustomerId())
                .isPresent();
        if (!existAccount) {
            Account accountSaved = mapAccountToRequest(accountRequest);
            repository.save(accountSaved);
            return "Account successful saved";
        }
        return "Account already exist";
    }

    /**
     * Convert entity get from DB to response DTO
     * @param account Funded from DB
     * @return Mapper AccountResponse
     */
    AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .address(account.getAddress())
                .createDate(LocalDate.now())
                .build();
    }

    /**
     * Convert input data to Account Entity
     * @param request Input data
     * @return Account entity
     */
    Account mapAccountToRequest(AccountRequest request) {
        return Account.builder()
                .customerId(request.getCustomerId())
                .accountType(request.getAccountType())
                .address(request.getAddress())
                .accountNumber(request.getAccountNumber())
                .createDate(LocalDate.now())
                .build();
    }
}
