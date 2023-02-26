package com.bank.account.service;

import com.bank.account.dto.AccountRequest;
import com.bank.account.dto.AccountResponse;
import com.bank.account.model.Account;
import com.bank.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class AccountServiceTest {

    @MockBean
    private AccountRepository repository;

    @Autowired
    private AccountService service;

    @Test
    void shouldGetOneAccountFromDbByCustomerId() {
        String customerId = "1d-aijf";
        Account accountExpected = getDummyAccount();

        when(repository.findAccountByCustomerId(anyString()))
                .thenReturn(Optional.of(accountExpected));
        AccountResponse response = service.getAccountByCustomerId(customerId);

        assertNotNull(response.getAccountNumber());
        assertEquals(response.getAccountNumber(), accountExpected.getAccountNumber());

    }

    @Test
    void shouldGetAllAccountsFromDb() {
        when(repository.findAll()).thenReturn(getDummyAccountList());
        List<AccountResponse> result = service.getAllAccounts();

        assertEquals(result.size(), 3);
        assertEquals(result.get(1).getAddress(), "address 2");
    }

    @Test
    void shouldGetEmptyAccountList() {
        when(repository.findAll()).thenReturn(Arrays.asList());
        List<AccountResponse> result = service.getAllAccounts();

        assertEquals(result.size(), 0);
        assertEquals(result, Arrays.asList());
    }

    @Test
    void shouldSaveOnceTimeAccount() {
        List<Account> firstGet = repository.findAll();
        when(repository.findAccountByCustomerId("C001"))
                .thenReturn(Optional.empty());
        when(repository.save(getDummyAccount())).thenReturn(getDummyAccount());
        String expected = service.saveAccount(getDummyAccountRequest());

        assertEquals(firstGet.size(), 0);
        assertEquals(expected, "Account successful saved");
    }

    @Test
    void shouldSaveAccountThatAlreadyExist() {
        repository.save(getDummyAccount());
        when(repository.findAccountByCustomerId("C001"))
                .thenReturn(Optional.of(getDummyAccount()));
        when(repository.save(getDummyAccount())).thenReturn(getDummyAccount());
        String expected = service.saveAccount(getDummyAccountRequest());

        assertEquals(expected, "Account already exist");
    }

    private Account getDummyAccount() {
        return Account.builder()
                .accountNumber("account-num-01")
                .customerId("C001")
                .accountNumber("123")
                .accountType("NOMINA")
                .address("AV EJERCITO")
                .createDate(LocalDate.now())
                .build();
    }

    private AccountRequest getDummyAccountRequest() {
        return AccountRequest.builder()
                .customerId("C001")
                .accountNumber("account-num-01")
                .accountNumber("123")
                .accountType("NOMINA")
                .address("AV EJERCITO")
                .build();
    }

    private List<Account> getDummyAccountList() {
        return Arrays.asList(
            new Account("A1", "AC01", "CC", "address 1", LocalDate.now()),
            new Account("A2", "AC02", "CC", "address 2", LocalDate.now()),
            new Account("A3", "AC03", "CC", "address 3", LocalDate.now())
        );
    }
}