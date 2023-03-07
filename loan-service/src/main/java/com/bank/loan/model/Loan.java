package com.bank.loan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private int loanNumber;
    private int customerId;
    private LocalDate startDt;
    private String loanType;
    private BigDecimal totalLoan;
    private BigDecimal amountPaid;
    private int outstandingAmount;
    private LocalDate createDate;
}
