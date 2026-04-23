package com.hackathon.notificacao.email.dto;

public record EmailDetailDTO(
        String to,
        String subject,
        String body
) {
}
