package com.bank.loan.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Loan(
                rs.getInt("loan_number"),
                rs.getInt("customer_id"),
                rs.getDate("start_dt").toLocalDate(),
                rs.getString("loan_type"),
                rs.getBigDecimal("total_loan"),
                rs.getBigDecimal("amount_paid"),
                rs.getInt("outstanding_amount"),
                rs.getDate("create_date").toLocalDate()
        );
    }
}
