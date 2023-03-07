package com.bank.loan.exception;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
}
