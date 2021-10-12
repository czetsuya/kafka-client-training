package com.czetsuyatech.kafka.service.listener;

import com.czetsuyatech.kafka.admin.config.KafkaConfigData;
import com.czetsuyatech.kafka.avro.model.RandomTempAvroModel;
import com.czetsuyatech.kafka.producer.config.com.czetsuyatech.kafka.producer.service.KafkaProducer;
import com.czetsuyatech.kafka.service.dto.TempDto;
import com.czetsuyatech.kafka.service.transformer.RandomTempToAvroTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RandomTempKafkaStatusListener {

  private static final Logger LOG = LoggerFactory.getLogger(RandomTempKafkaStatusListener.class);

  private final KafkaConfigData kafkaConfigData;
  private final KafkaProducer<Long, RandomTempAvroModel> kafkaProducer;
  private final RandomTempToAvroTransformer randomTempToAvroTransformer;

  public RandomTempKafkaStatusListener(KafkaConfigData configData,
      KafkaProducer<Long, RandomTempAvroModel> producer,
      RandomTempToAvroTransformer transformer) {

    this.kafkaConfigData = configData;
    this.kafkaProducer = producer;
    this.randomTempToAvroTransformer = transformer;
  }

  public void onStatus(TempDto wordDto) {

    LOG.info("Received temp {} sending to kafka topic {}", wordDto.getTemp(), kafkaConfigData.getTopicName());
    RandomTempAvroModel avroModel = randomTempToAvroTransformer.getRandomAvroModelFromDto(wordDto);
    kafkaProducer.send(kafkaConfigData.getTopicName(), avroModel.getId(), avroModel);
  }
}
