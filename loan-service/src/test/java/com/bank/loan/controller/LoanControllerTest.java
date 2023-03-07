package com.bank.loan.controller;

import com.bank.loan.model.Loan;
import com.bank.loan.repository.LoanDao;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "sql.loan.select.all= SELECT * FROM loan"
})
@Testcontainers
@AutoConfigureMockMvc
class LoanControllerTest {

    @Container
    static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:8")
            .withDatabaseName("bankdb")
            .withPassword("secret2")
            .withInitScript("db/init.sql");

    @Autowired
    private LoanDao repository;

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void setDynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    /* ~ ENDPOINTS
    ------------------------------- */
    private static final String GET_ALL_LOANS = "/loan/v0/loan";
    private static final String GET_LOAN_BY_CUSTOMER = "/loan/v0/loan/1";

    private static final String POST_SAVE_LOAN = "/loan/v0/loan";
    private static final String DELETE_LOAN_BY_CUSTOMER = "/loan/v0/loan/5";

    private static final String DELETE_LOAN_NOT_EXIST = "/loan/v0/loan/500";

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void getAllLoans() throws Exception {
        Assertions.assertTrue(repository.getLoans().isEmpty());
        getDummyListLoan().stream().forEach(loan -> repository.insertLoan(loan));

        mockMvc.perform(get(GET_ALL_LOANS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    void getEmptyListLoans() throws Exception {
        Assertions.assertTrue(repository.getLoans().isEmpty());

        mockMvc.perform(get(GET_ALL_LOANS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldGetLoanByCustomerId() throws Exception {
        Assertions.assertTrue(repository.getLoans().isEmpty());
        repository.insertLoan(getDummyListLoan().get(0));
        mockMvc.perform(get(GET_LOAN_BY_CUSTOMER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanType",
                        Matchers.hasToString("PERSONAL")));
    }

    @Test
    void shouldSaveSuccessLoan() throws Exception {
        Assertions.assertTrue(repository.getLoans().isEmpty());
        mockMvc.perform(post(POST_SAVE_LOAN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"customerId\": 1, \"startDt\": \"2023-02-20\", \"loanType\": \"Personal\", " +
                        "\"totalLoan\": 10000, \"amountPaid\": 3500, \"outstandingAmount\": 6500, \"createDate\": \"2023-02-20\" }")
        ).andExpect(status().isCreated());

        Assertions.assertNotNull(repository.getLoanByCustomerId(1));
        Assertions.assertEquals(repository.getLoans().size(), 1);
    }

    @Test
    void shouldReturnsErrorWhenDeleteLoanByNumber() throws Exception {
        Assertions.assertTrue(repository.getLoans().isEmpty());
        getDummyListLoan().stream().forEach(loan -> repository.insertLoan(loan));

        mockMvc.perform(delete(DELETE_LOAN_NOT_EXIST))
                .andExpect(status().isNotFound());

        Assertions.assertEquals(repository.getLoans().size(), 2);
    }

    private List<Loan> getDummyListLoan() {
        return Arrays.asList(
                new Loan(1, 1, LocalDate.now(), "PERSONAL", BigDecimal.valueOf(1234), BigDecimal.valueOf(321), 213, LocalDate.now()),
                new Loan(2, 2, LocalDate.now(), "FINANCE", BigDecimal.valueOf(234), BigDecimal.valueOf(21), 212, LocalDate.now())
        );
    }
}