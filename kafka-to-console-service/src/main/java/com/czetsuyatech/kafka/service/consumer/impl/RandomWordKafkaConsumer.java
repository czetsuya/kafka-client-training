package com.czetsuyatech.kafka.service.consumer.impl;

import com.czetsuyatech.kafka.admin.client.KafkaAdminClient;
import com.czetsuyatech.kafka.admin.config.KafkaConfigData;
import com.czetsuyatech.kafka.avro.model.RandomWordAvroModel;
import com.czetsuyatech.kafka.service.consumer.KafkaConsumer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RandomWordKafkaConsumer implements KafkaConsumer<Long, RandomWordAvroModel> {

  private static final Logger LOG = LoggerFactory.getLogger(RandomWordKafkaConsumer.class);

  private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
  private final KafkaAdminClient kafkaAdminClient;
  private final KafkaConfigData kafkaConfigData;

  public RandomWordKafkaConsumer(KafkaListenerEndpointRegistry listenerEndpointRegistry,
      KafkaAdminClient adminClient,
      KafkaConfigData configData) {
    this.kafkaListenerEndpointRegistry = listenerEndpointRegistry;
    this.kafkaAdminClient = adminClient;
    this.kafkaConfigData = configData;
  }

  @EventListener
  public void onAppStarted(ApplicationStartedEvent event) {

    kafkaAdminClient.checkTopicsCreated();
    LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
    kafkaListenerEndpointRegistry.getListenerContainer("randomWordsTopicListener").start();
  }

  @Override
  @KafkaListener(id = "randomWordsTopicListener", topics = "${kafka-config.topic-name}")
  public void receive(@Payload List<RandomWordAvroModel> messages,
      @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Long> keys,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

    LOG.info("{} number of message received with keys {}, partitions {} and offsets {}, Thread id {}",
        messages.size(),
        keys.toString(),
        partitions.toString(),
        offsets.toString(),
        Thread.currentThread().getId());
  }
}

