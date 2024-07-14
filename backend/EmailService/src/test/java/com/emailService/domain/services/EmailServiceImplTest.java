package com.emailService.domain.services;

import com.emailService.core.domain.model.Email;
import com.emailService.core.domain.model.EmailType;
import com.emailService.core.domain.model.order.ContactDetails;
import com.emailService.core.domain.model.order.Order;
import com.emailService.core.domain.services.impl.EmailServiceImpl;
import com.emailService.core.domain.services.interfaces.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ITemplateEngine htmlTemplateEngine;
    @Mock
    private EmailRepository emailRepository;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendOrderConfirmationEmail() throws MessagingException, UnsupportedEncodingException {
        // create sample order
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setUserId("sampleUserId");
        order.setDate("2024-07-14");
        order.setTotalAmount(new BigDecimal("199.99"));
        order.setOrderItems(Collections.emptyList());
        order.setShippingAddress(null);
        order.setBillingAddress(null);
        order.setContactDetails(new ContactDetails("123-456-7890", "customer@example.com"));

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Context context = new Context(Locale.getDefault());
        context.setVariable("order", order);
        context.setVariable("logo", "images/logo.png");
        when(htmlTemplateEngine.process(eq("order-confirmation"), any(Context.class))).thenReturn("htmlContent");

        // send email
        emailService.sendOrderConfirmationEmail(order);

        ArgumentCaptor<MimeMessageHelper> mimeMessageHelperCaptor = ArgumentCaptor.forClass(MimeMessageHelper.class);
        verify(mailSender).send(mimeMessage);
        verify(htmlTemplateEngine).process(eq("order-confirmation"), any(Context.class));

        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(emailRepository).save(emailCaptor.capture());

        // check if the email was saved correctly
        Email savedEmail = emailCaptor.getValue();
        assertEquals("customer@example.com", savedEmail.getRecipientEmail());
        assertEquals("sampleUserId", savedEmail.getRecipientUserId());
        assertEquals(EmailType.ORDER_CONFIRMATION, savedEmail.getEmailType());
        assertNotNull(savedEmail.getDateSent());
    }
}
