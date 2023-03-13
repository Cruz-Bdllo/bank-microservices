package com.bank.loan.repository;

import com.bank.loan.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanDao {
    List<Loan> getLoans();
    List<Loan> getLoansByCustomerId(int customerId);
    Optional<Loan> getLoanByNumber(int loanNumber);
    int insertLoan(Loan loan);
    int deleteLoan(int loanId);

    int deleteAll();
}
