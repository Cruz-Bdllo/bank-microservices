package com.bank.loan.repository;

import com.bank.loan.model.Loan;
import com.bank.loan.model.LoanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LoanDaoImp implements LoanDao {

    private final JdbcTemplate jdbcTemplate; // Injected
    @Value("${sql.loan.select.all}")
    private final String SELECT_ALL_LOANS;

    @Value("${sql.loan.select.by.customer}")
    private final String SELECT_ONE_LOAN;

    @Value("${sql.loan.select.by.number}")
    private final String SELECT_LOAN_BY_NUMBER;

    @Value("${sql.loan.insert}")
    private final String INSERT_LOAN;

    @Value("${sql.loan.delete}")
    private final String DELETE_LOAN;

    @Override
    public List<Loan> getLoans() {
        return jdbcTemplate.query(SELECT_ALL_LOANS,
                new LoanMapper());
    }

    @Override
    public Optional<Loan> getLoanByCustomerId(int customerId) {
        return jdbcTemplate.query(SELECT_ONE_LOAN, new LoanMapper())
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Loan> getLoanByNumber(int loanNumber) {
        return jdbcTemplate.query(SELECT_LOAN_BY_NUMBER, new LoanMapper())
                .stream()
                .findFirst();
    }

    @Override
    public int insertLoan(Loan loan) {
        return jdbcTemplate.update(INSERT_LOAN,
            loan.getCustomerId(),
            loan.getStartDt(),
            loan.getLoanType(),
            loan.getTotalLoan(),
            loan.getAmountPaid(),
            loan.getOutstandingAmount(),
            loan.getCreateDate()
        );
    }

    @Override
    public int deleteLoan(int loanNumber) {
        return jdbcTemplate.update(DELETE_LOAN, loanNumber);
    }
}
