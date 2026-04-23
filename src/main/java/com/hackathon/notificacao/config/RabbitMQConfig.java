package com.hackathon.notificacao.config;

import com.hackathon.notificacao.email.dto.ConsultEvent;
import com.hackathon.notificacao.email.dto.NotificationEvent;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@EnableRabbit
@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "estoque.events";
    public static final String EMAIL_QUEUE = "notification.email.queue";

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(
                EMAIL_QUEUE,
                true,
                false,
                false
        );
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(exchange());
    }

    @Bean
    public MessageConverter messageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();

        DefaultJacksonJavaTypeMapper typeMapper = new DefaultJacksonJavaTypeMapper();
        typeMapper.setTypePrecedence(JacksonJavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("*");
        typeMapper.setIdClassMapping(Map.of(
                "notificationEvent", NotificationEvent.class,
                "consultEvent", ConsultEvent.class
        ));

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
