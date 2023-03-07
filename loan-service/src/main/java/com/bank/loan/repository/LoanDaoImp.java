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
    private final String SELECT_ALL_LOANS = "SELECT * FROM loan";

    private final String SELECT_ONE_LOAN = "SELECT * FROM loan WHERE customer_id = ?";

    private final String SELECT_LOAN_BY_NUMBER = "SELECT * FROM loan WHERE loan_number = ?";

    private final String INSERT_LOAN = "INSERT INTO loan (customer_id, start_dt, loan_type, total_loan, amount_paid, outstanding_amount, create_date) VALUES ( ?, ?, ?, ?, ?, ?, ?)";

    private final String DELETE_LOAN = "DELETE FROM loan where loan_number = ?";

    private final String DELETE_ALL = "DELETE FROM loan";

    @Override
    public List<Loan> getLoans() {
        return jdbcTemplate.query(SELECT_ALL_LOANS,
                new LoanMapper());
    }

    @Override
    public Optional<Loan> getLoanByCustomerId(int customerId) {
        return jdbcTemplate.query(SELECT_ONE_LOAN, new LoanMapper(), customerId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Loan> getLoanByNumber(int loanNumber) {
        return jdbcTemplate.query(SELECT_LOAN_BY_NUMBER, new LoanMapper(), loanNumber)
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

    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL);
    }
}
