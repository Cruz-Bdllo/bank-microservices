package com.bank.account.dto;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountResponse {
    private int accountNumber;
    private String accountType;
    private String address;
    private LocalDate createDate;
}
