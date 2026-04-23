package com.hackathon.notificacao.email.service;

import com.hackathon.notificacao.email.dto.ConsultEvent;
import com.hackathon.notificacao.email.dto.NotificationEvent;
import com.hackathon.notificacao.email.exception.EmailException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailTemplateService emailTemplateService;
    @Value("${app.mail.from}")
    private final String from;

    public EmailService(JavaMailSender mailSender, EmailTemplateService emailTemplateService,@Value("${app.mail.from}") String from) {
        this.mailSender = mailSender;
        this.emailTemplateService = emailTemplateService;
        this.from = from;
    }

    private void sendEmail(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =  new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
        } catch (Exception ex) {
            throw new EmailException("Error while sending email: ", ex);
        }
    }

    public void sendCreated(NotificationEvent notificationEvent) {
        sendEmail(
                notificationEvent.toEmail(),
                "Entity created",
                emailTemplateService.created(notificationEvent)
        );
    }

    public void sendUpdated(NotificationEvent notificationEvent) {
        sendEmail(
                notificationEvent.toEmail(),
                "Entity updated",
                emailTemplateService.updated(notificationEvent)
        );
    }

    public void sendDeleted(NotificationEvent notificationEvent) {
        sendEmail(
                notificationEvent.toEmail(),
                "Entity deleted",
                emailTemplateService.deleted(notificationEvent)
        );
    }

    public void sendConsult(ConsultEvent consultEvent) {
        sendEmail(
                consultEvent.toEmail(),
                "Consultation results",
                emailTemplateService.consult(consultEvent)
        );
    }

    public void sendExpired(ConsultEvent consultEvent){
        sendEmail(
                consultEvent.toEmail(),
                "Inventory expired results",
                emailTemplateService.expired(consultEvent)
        );
    }

    public void sendLowStock(ConsultEvent consultEvent){
        sendEmail(
                consultEvent.toEmail(),
                "Inventory expired results",
                emailTemplateService.lowStock(consultEvent)
        );
    }
}
