package com.emailService.core.domain.services.impl;

import com.emailService.core.domain.model.Email;
import com.emailService.core.domain.model.EmailType;
import com.emailService.core.domain.model.order.Order;
import com.emailService.core.domain.services.interfaces.EmailRepository;
import com.emailService.core.domain.services.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;


import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String TEMPLATE_NAME = "order-confirmation";
    private static final String MAIL_SUBJECT = "Order Confirmation";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ITemplateEngine htmlTemplateEngine;

    @Autowired
    private EmailRepository emailRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${mail.from.name}")
    private String fromName;

    public void sendOrderConfirmationEmail(Order order) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(order.getContactDetails().getContactEmail());
        email.setSubject(MAIL_SUBJECT);
        email.setFrom(new InternetAddress(fromEmail, fromName));

        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("order", order);

        final String htmlContent = htmlTemplateEngine.process(TEMPLATE_NAME, ctx);
        email.setText(htmlContent, true);

        mailSender.send(mimeMessage);

        saveEmail(order.getContactDetails().getContactEmail(), order.getUserId(), EmailType.ORDER_CONFIRMATION);
    }

    private void saveEmail(String recipientEmail, UUID recipientUserId, EmailType emailType) {
        Email email = new Email();
        email.setRecipientEmail(recipientEmail);
        email.setRecipientUserId(recipientUserId);
        email.setDateSent(LocalDateTime.now());
        email.setEmailType(emailType);

        emailRepository.save(email);
    }
}
