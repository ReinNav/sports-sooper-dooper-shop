package com.cartService.port.user.advice;

import com.cartService.core.domain.service.exception.CartItemNotFound;
import com.cartService.port.user.exception.InvalidQuantityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CartAdvice {

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<String> handleInvalidQuantityException(InvalidQuantityException ex) {
        return new ResponseEntity<>("Invalid quantity of items. Quantity should not be negative, zero or infinity.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartItemNotFound.class)
    public ResponseEntity<String> handleCartItemNotFoundException(CartItemNotFound ex) {
        return new ResponseEntity<>("No such item found in cart.", HttpStatus.BAD_REQUEST);
    }

}
