package com.bank.loan.controller;

import com.bank.loan.exception.ErrorResponse;
import com.bank.loan.exception.NotFoundException;
import com.bank.loan.model.Loan;
import com.bank.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/loan/v0")
public class LoanController {

    private final LoanService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/loan")
    public List<Loan> getAllLoans() {
        return service.getAllLoans();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/loan/{customerId}")
    public Loan getLoanByCustomerId(@PathVariable int customerId) {
        return service.getLoanByCustomerId(customerId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/loan")
    public void saveLoan(@RequestBody Loan loan) {
        service.saveLoan(loan);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/loan/{loanNumber}")
    public void deleteLoan(@PathVariable int loanNumber) {
        service.deleteLoanByNumber(loanNumber);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse loanNotFound(NotFoundException exception) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
