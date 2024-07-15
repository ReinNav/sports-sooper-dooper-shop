package com.emailService.core.domain.services.interfaces;

import com.emailService.core.domain.model.EmailType;
import com.emailService.core.domain.model.order.Order;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    public void sendOrderConfirmationEmail(Order order) throws MessagingException, UnsupportedEncodingException;
}
