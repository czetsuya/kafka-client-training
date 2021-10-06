package com.czetsuyatech.kafka.service.listener;

import com.czetsuyatech.kafka.admin.config.KafkaConfigData;
import com.czetsuyatech.kafka.avro.model.RandomWordAvroModel;
import com.czetsuyatech.kafka.producer.config.com.czetsuyatech.kafka.producer.service.KafkaProducer;
import com.czetsuyatech.kafka.service.dto.WordDto;
import com.czetsuyatech.kafka.service.transformer.RandomWordToAvroTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RandomWordsKafkaStatusListener {

  private static final Logger LOG = LoggerFactory.getLogger(RandomWordsKafkaStatusListener.class);

  private final KafkaConfigData kafkaConfigData;
  private final KafkaProducer<Long, RandomWordAvroModel> kafkaProducer;
  private final RandomWordToAvroTransformer randomWordToAvroTransformer;

  public RandomWordsKafkaStatusListener(KafkaConfigData configData,
      KafkaProducer<Long, RandomWordAvroModel> producer,
      RandomWordToAvroTransformer transformer) {

    this.kafkaConfigData = configData;
    this.kafkaProducer = producer;
    this.randomWordToAvroTransformer = transformer;
  }

  public void onStatus(WordDto wordDto) {

    LOG.info("Received status text {} sending to kafka topic {}", wordDto.getWord(), kafkaConfigData.getTopicName());
    RandomWordAvroModel avroModel = randomWordToAvroTransformer.getRandomAvroModelFromDto(wordDto);
    kafkaProducer.send(kafkaConfigData.getTopicName(), avroModel.getUserId(), avroModel);
  }
}
