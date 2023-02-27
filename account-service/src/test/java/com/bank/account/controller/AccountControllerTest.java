package com.bank.account.controller;

import com.bank.account.model.Account;
import com.bank.account.repository.AccountRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class AccountControllerTest {

    @Container
    static MySQLContainer mysql = new MySQLContainer("mysql:8")
            .withDatabaseName("bankdb")
            .withPassword("secret2");

    @Autowired
    private AccountRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    /* ~ ENDPOINTS
    ------------------------------- */
    private static final String GET_ACCOUNT = "/account/v0/account/1";
    private static final String GET_ACCOUNT_LIST = "/account/v0/account";



    @DynamicPropertySource
    static void setDynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Test
    void shouldGETAccountByCustomerId() throws Exception {
        Assertions.assertTrue(repository.findAll().isEmpty());
        repository.save(getDummyAccount());

        mockMvc.perform(get(GET_ACCOUNT)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType", Matchers.hasToString("NOMINA")));
    }

    @Test
    void shouldGetEmptyAccountByCustomerIdWhenCustomerNotExist() throws Exception {
        Assertions.assertTrue(repository.findAll().isEmpty());
        mockMvc.perform(get("/account/v0/account/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    void shouldGETAccountList() throws Exception {
        Assertions.assertTrue(repository.findAll().isEmpty());
        getDummyAccountList().stream().forEach(account -> repository.save(account));

        mockMvc.perform(get(GET_ACCOUNT_LIST)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    void shouldGETAccountEmptyList() throws Exception {
        Assertions.assertTrue(repository.findAll().isEmpty());

        mockMvc.perform(get(GET_ACCOUNT_LIST)
                )
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").isEmpty());
    }

    private Account getDummyAccount() {
        return Account.builder()
                .accountNumber(1)
                .customerId(1)
                .accountNumber(1)
                .accountType("NOMINA")
                .address("AV EJERCITO")
                .createDate(LocalDate.now())
                .build();
    }

    private List<Account> getDummyAccountList() {
        return Arrays.asList(
                new Account(2, 1, "NOMINA", "address 1", LocalDate.now()),
                new Account(3, 2, "LIBRETON", "address 2", LocalDate.now()),
                new Account(4, 3, "DIGITAL", "address 3", LocalDate.now())
        );
    }

}