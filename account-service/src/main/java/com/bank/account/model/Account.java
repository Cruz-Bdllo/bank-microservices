package com.bank.account.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Account {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerId;

    @Column(name = "account_number")
    private int accountNumber;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "address")
    private String address;

    @Column(name = "create_date")
    private LocalDate createDate;
}
