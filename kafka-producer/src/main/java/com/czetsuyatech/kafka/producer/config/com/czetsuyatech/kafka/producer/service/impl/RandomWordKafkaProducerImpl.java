package com.czetsuyatech.kafka.producer.config.com.czetsuyatech.kafka.producer.service.impl;

import com.czetsuyatech.kafka.avro.model.RandomWordAvroModel;
import com.czetsuyatech.kafka.producer.config.com.czetsuyatech.kafka.producer.service.KafkaProducer;
import javax.annotation.PreDestroy;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class RandomWordKafkaProducerImpl implements KafkaProducer<Long, RandomWordAvroModel> {

  private static final Logger LOG = LoggerFactory.getLogger(RandomWordKafkaProducerImpl.class);

  private KafkaTemplate<Long, RandomWordAvroModel> kafkaTemplate;

  public RandomWordKafkaProducerImpl(KafkaTemplate<Long, RandomWordAvroModel> template) {
    this.kafkaTemplate = template;
  }

  @Override
  public void send(String topicName, Long key, RandomWordAvroModel message) {

    LOG.info("Sending message='{}' to topic='{}'", message, topicName);
    ListenableFuture<SendResult<Long, RandomWordAvroModel>> kafkaResultFuture =
        kafkaTemplate.send(topicName, key, message);
    addCallback(topicName, message, kafkaResultFuture);
  }

  @PreDestroy
  public void close() {

    if (kafkaTemplate != null) {
      LOG.info("Closing kafka producer!");
      kafkaTemplate.destroy();
    }
  }

  private void addCallback(String topicName, RandomWordAvroModel message,
      ListenableFuture<SendResult<Long, RandomWordAvroModel>> kafkaResultFuture) {

    kafkaResultFuture.addCallback(new ListenableFutureCallback<>() {

      @Override
      public void onFailure(Throwable throwable) {
        LOG.error("Error while sending message {} to topic {}", message.toString(), topicName, throwable);
      }

      @Override
      public void onSuccess(SendResult<Long, RandomWordAvroModel> result) {

        RecordMetadata metadata = result.getRecordMetadata();
        LOG.debug("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
            metadata.topic(),
            metadata.partition(),
            metadata.offset(),
            metadata.timestamp(),
            System.nanoTime());
      }
    });
  }
}
