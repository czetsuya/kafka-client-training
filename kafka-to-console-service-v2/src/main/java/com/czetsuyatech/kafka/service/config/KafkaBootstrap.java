package com.czetsuyatech.kafka.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@Configuration
@EnableKafka
public class KafkaBootstrap {

  @Bean
  public LoggingErrorHandler errorHandler() {
    return new LoggingErrorHandler();
  }

  @Bean
  public KafkaListenerContainerFactory<?> kafkaJsonListenerContainerFactory(
      ConsumerFactory<Object, Object> consumerFactory) {

    ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setAckDiscarded(true);
    factory.setErrorHandler(new KafkaErrorHandler());
    return factory;
  }
}
