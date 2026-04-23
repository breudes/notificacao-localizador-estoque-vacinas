package com.hackathon.notificacao.email.dto;

import com.hackathon.notificacao.email.enums.EventType;
import java.time.Instant;

public record NotificationEvent(
        // Entity: HealthFacility, Inventory or Vaccine
        Long entityId,
        String entityType,
        EventType eventType,
        // Person to be notified
        String toEmail,
        // Person who made some action (admin)
        String adminEmail,
        Instant eventDate
) {
}
