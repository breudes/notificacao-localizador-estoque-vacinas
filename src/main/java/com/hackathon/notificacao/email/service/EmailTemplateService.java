package com.hackathon.notificacao.email.service;

import com.hackathon.notificacao.email.dto.ConsultEvent;
import com.hackathon.notificacao.email.dto.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.format.DateTimeFormatter;

@Service
public class EmailTemplateService {
    @Autowired
    private final TemplateEngine templateEngine;

    public EmailTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String created(NotificationEvent event){
        return process("email/created", "notification", event, null);
    }

    public String updated(NotificationEvent event) {
        return process("email/updated", "notification", event, null);
    }

    public String deleted(NotificationEvent event) {
        return process("email/deleted", "notification", event, null);
    }

    public String consult(ConsultEvent event) {
        return process("email/consult", "consult", null, event);

    }

    public String expired(ConsultEvent event) {
        return process("email/expired", "consult", null, event);
    }

    public String lowStock(ConsultEvent event) {
        return process("email/low-stock", "consult", null, event);
    }

    private String process(String template, String typeEvent, NotificationEvent notificationEvent, ConsultEvent consultEvent) {
        Context context = new Context();

        if(typeEvent.equalsIgnoreCase("notification")){
            context.setVariable("entityId", notificationEvent.entityId());
            context.setVariable("eventType", notificationEvent.eventType());
            context.setVariable("toEmail", notificationEvent.toEmail());
            context.setVariable("adminEmail", notificationEvent.adminEmail());
            context.setVariable("eventDate", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .format(consultEvent.eventDate()));
        } else {
            context.setVariable("healthFacilitiesId", consultEvent.healthFacilitiesId());
            context.setVariable("inventoriesId", consultEvent.inventoriesId());
            context.setVariable("vaccinesId", consultEvent.vaccinesId());
            context.setVariable("eventType", consultEvent.eventType());
            context.setVariable("toEmail", consultEvent.toEmail());
            context.setVariable("eventDate", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .format(consultEvent.eventDate()));
        }
        return templateEngine.process(template, context);
    }
}
