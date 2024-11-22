package com.droveda.azure_labs.config;

import jakarta.jms.ConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@Configuration
@EnableJms
public class JmsConfig {

    @Value("${ACTIVEMQ_BROKER_URL:amqp://localhost:5672}")
    private String activeMqBrokerUrl;

    @Value("${spring.jms.servicebus.connection-string}")
    private String serviceBusConnectionString;

    @Value("${spring.jms.servicebus.policy-name}")
    private String policyName;

    @Value("${spring.jms.servicebus.policy-key}")
    private String policyKey;

    @Bean
    @Profile("default")
    public ConnectionFactory connectionFactoryActiveMQ() {
        var connectionFactory = new JmsConnectionFactory(activeMqBrokerUrl);
        return getCachingConnectionFactory(connectionFactory);
    }

    @Bean
    @Profile("!default")
    public ConnectionFactory connectionFactoryServiceBus() {
        var connectionFactory = new JmsConnectionFactory(serviceBusConnectionString);
        connectionFactory.setUsername(policyName);
        connectionFactory.setPassword(policyKey);

        return getCachingConnectionFactory(connectionFactory);
    }

    private CachingConnectionFactory getCachingConnectionFactory(JmsConnectionFactory connectionFactory) {
        var cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
        cachingConnectionFactory.setSessionCacheSize(10);
        return cachingConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(final ConnectionFactory connectionFactory, final MappingJackson2MessageConverter converter) {
        var jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(converter);

        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory(final ConnectionFactory connectionFactory,
                                                                                 final MappingJackson2MessageConverter converter) {
        var factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);

        return factory;
    }

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}
