package com.paymentService.core.domain.exception;

public class PaymentCouldNotCompleteException extends RuntimeException {
    public PaymentCouldNotCompleteException(String message) {
        super(message);
    }

    public PaymentCouldNotCompleteException(String message, Throwable cause) {
        super(message, cause);
    }
}