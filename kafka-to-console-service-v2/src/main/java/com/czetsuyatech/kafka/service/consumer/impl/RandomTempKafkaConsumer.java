package com.czetsuyatech.kafka.service.consumer.impl;

import com.czetsuyatech.kafka.avro.model.RandomTempAvroModel;
import com.czetsuyatech.kafka.service.consumer.KafkaConsumer;
import com.czetsuyatech.kafka.service.transformer.AvroToDtoTransformer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomTempKafkaConsumer implements KafkaConsumer<Long, RandomTempAvroModel> {

  private static final Logger LOG = LoggerFactory.getLogger(RandomTempKafkaConsumer.class);
  private final AvroToDtoTransformer transformer;

  @Override
  @KafkaListener(id = "randomTempTopicListener", topics = "${kafka-config.topic-name}",
      groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaJsonListenerContainerFactory")
  public void receive(@Payload List<RandomTempAvroModel> messages,
      @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Long> keys,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

    LOG.info("[v2] {} number of message received with keys {}, partitions {} and offsets {}, Thread id {}",
        messages.size(),
        keys.toString(),
        partitions.toString(),
        offsets.toString(),
        Thread.currentThread().getId());

    LOG.info("[v2] messages received={}", transformer.getTempModels(messages));
  }
}

