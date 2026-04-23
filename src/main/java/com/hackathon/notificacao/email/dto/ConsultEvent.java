package com.hackathon.notificacao.email.dto;

import com.hackathon.notificacao.email.enums.EventType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record ConsultEvent(
        List<Long> healthFacilitiesId,
        List<Long> inventoriesId,
        List<Long> vaccinesId,
        EventType eventType,
        String entityType,
        String toEmail,
        Instant eventDate
) {
}
