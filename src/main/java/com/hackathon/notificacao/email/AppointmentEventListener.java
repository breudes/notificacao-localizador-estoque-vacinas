package com.hackathon.notificacao.email;

import com.hackathon.notificacao.config.RabbitMQConfig;
import com.hackathon.notificacao.email.dto.ConsultEvent;
import com.hackathon.notificacao.email.dto.NotificationEvent;
import com.hackathon.notificacao.email.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventListener {
    @Autowired
    private final EmailService emailService;

    public AppointmentEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consumeEvent(@Payload Object event) {
        if(event instanceof NotificationEvent notificationEvent){
            try{
                System.out.println("📧 Sending email to: " + notificationEvent.toEmail());
                System.out.println("Action was in:  " + notificationEvent.eventDate());
                System.out.println("Event type:  " + notificationEvent.eventType());
                switch (notificationEvent.eventType()) {
                    case HEALTHFACILITY_CREATED, INVENTORY_CREATED -> emailService.sendCreated(notificationEvent);
                    case HEALTHFACILITY_UPDATED, INVENTORY_UPDATED -> emailService.sendUpdated(notificationEvent);
                    case HEALTHFACILITY_DELETED, INVENTORY_DELETED -> emailService.sendDeleted(notificationEvent);
                }
            } catch (Exception ex) {
                System.out.println("Erro ao enviar e-mail para evento - " + ex.getMessage());
            }
        } else if (event instanceof ConsultEvent consultEvent){
            try{
                System.out.println("📧 Sending email to: " + consultEvent.toEmail());
                System.out.println("Consult in:  " + consultEvent.eventDate());
                System.out.println("Event type:  " + consultEvent.eventType());
                switch (consultEvent.eventType()){
                    case CONSULT_BY_CNES,
                         CONSULT_BY_ADDRESS,
                         CONSULT_BY_HEALTHFACILITY_NAME,
                         CONSULT_BY_VACCINE_NAME,
                         CONSULT_BY_HEALTHFACILITY_NAME_AND_VACCINE_NAME,
                         CONSULT_VACCINE_BY_NAME,
                         CONSULT_VACCINE_BY_DISEASE,
                         CONSULT_VACCINE_BY_LIFESTAGE,
                         CONSULT_VACCINE_BY_AGERANGE -> emailService.sendConsult(consultEvent);
                    case INVENTORY_EXPIRED -> emailService.sendExpired(consultEvent);
                    case INVENTORY_LOW_STOCK -> emailService.sendLowStock(consultEvent);
                }
            } catch (Exception ex) {
                System.out.println("Erro ao enviar e-mail para evento - " + ex.getMessage());
            }
        }
    }
}
