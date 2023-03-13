package com.bank.loan.service;

import com.bank.loan.exception.NotFoundException;
import com.bank.loan.model.Loan;
import com.bank.loan.repository.LoanDao;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class LoanServiceTest {

    @MockBean
    private LoanDao loanDao;

    @Autowired
    private LoanService loanService;


    @Test
    void shouldGetAllLoans() {
        when(loanDao.getLoans()).thenReturn(getDummyListLoan());

        List<Loan> result = loanService.getAllLoans();
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getLoanType(), "PERSONAL");
    }

    @Test
    void shouldGetLoansForCustomerId() {
        when(loanDao.getLoansByCustomerId(anyInt())).thenReturn(getDummyListLoan());
        List<Loan> result = loanService.getLoansByCustomerId(anyInt());
        assertNotNull(result);
        System.out.println("Tamanio del resultado alv => " + result.size());
        assertEquals(result.size(), 2);
    }

    @Test
    void shouldThrowExceptionWhenWentWrongInSaveLoan() {
        when(loanDao.insertLoan(getDummyLoan())).thenReturn(2);


        assertThrows(IllegalStateException.class,
                () -> loanService.saveLoan(getDummyLoan()));
    }



    @Test
    void shouldThrowExceptionWhenDeleteLoanNotExist() {
        when(loanDao.getLoanByNumber(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> loanService.deleteLoanByNumber(anyInt()));
    }

    @Test
    void shouldThrowExceptionWhenDeleteLoanIsWrong() {
        when(loanDao.getLoanByNumber(anyInt())).thenReturn(Optional.of(getDummyLoan()));
        when(loanDao.deleteLoan(anyInt())).thenReturn(2);

        assertThrows(IllegalStateException.class,
                () -> loanService.deleteLoanByNumber(anyInt()));
    }

    private Loan getDummyLoan() {
        return Loan.builder()
                .loanNumber(1)
                .customerId(1)
                .startDt(LocalDate.now())
                .loanType("PERSONAL")
                .totalLoan(BigDecimal.valueOf(1234))
                .amountPaid(BigDecimal.valueOf(4321))
                .outstandingAmount(213)
                .createDate(LocalDate.now())
                .build();
    }

    private List<Loan> getDummyListLoan() {
        return Arrays.asList(
                new Loan(1, 1, LocalDate.now(), "PERSONAL", BigDecimal.valueOf(1234), BigDecimal.valueOf(321), 213, LocalDate.now()),
                new Loan(2, 1, LocalDate.now(), "FINANCE", BigDecimal.valueOf(234), BigDecimal.valueOf(21), 212, LocalDate.now())
        );
    }
}