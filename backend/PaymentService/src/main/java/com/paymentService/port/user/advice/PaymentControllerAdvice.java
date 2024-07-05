package com.paymentService.port.user.advice;

import com.paymentService.core.domain.exception.PaymentCreationException;
import com.paymentService.core.domain.exception.PaymentNotFoundException;
import com.paymentService.core.domain.exception.PaymentCouldNotCompleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentControllerAdvice {

    // trying as thorough as possible as payment domain is critical

    @ExceptionHandler(PaymentCreationException.class)
    public ResponseEntity<String> handlePaymentCreationException(PaymentCreationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PaymentCouldNotCompleteException.class)
    public ResponseEntity<String> handlePaymentCompletionException(PaymentCouldNotCompleteException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
