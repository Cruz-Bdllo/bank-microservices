package com.bank.loan.service;

import com.bank.loan.exception.NotFoundException;
import com.bank.loan.model.Loan;
import com.bank.loan.repository.LoanDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanDao loanRepository; // Injected

    public List<Loan> getAllLoans() {
        return loanRepository.getLoans();
    }

    public List<Loan> getLoansByCustomerId(int customerId) {
        return loanRepository.getLoansByCustomerId(customerId);
    }

    public void saveLoan(Loan loan) {
        int saved = loanRepository.insertLoan(loan);
        if (saved != 1) {
            throw new IllegalStateException("something went wrong");
        }
    }

    public void deleteLoanByNumber(int loanNumber) {
        Optional<Loan> funded = loanRepository.getLoanByNumber(loanNumber);
        funded.ifPresentOrElse(loan -> {
                int result = loanRepository.deleteLoan(loanNumber);
                if (result != 1) {
                    throw new IllegalStateException("Oops something went wrong");
                }
            },
            () -> { throw new NotFoundException(String.format("Loan number %d not exist", loanNumber)); }
        );
    }
}
