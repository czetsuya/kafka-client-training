package com.czetsuyatech.kafka.service.consumer.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.czetsuyatech.kafka.avro.model.RandomTempAvroModel;
import com.czetsuyatech.kafka.service.consumer.IntegrationTest;
import com.czetsuyatech.kafka.service.consumer.fixtures.RandomTempAvroModelFixture;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.utils.ContainerTestUtils;

@Slf4j
class RandomTempKafkaConsumerIntegrationTest extends IntegrationTest {

  @MockBean
  private SchemaRegistryClient schemaRegistryClient;

  @BeforeEach
  void setup() {

    for (final MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
      log.debug("partitions per topic={}", kafkaEmbedded.getPartitionsPerTopic());
      log.debug("configuring listener {}", messageListenerContainer);
      ContainerTestUtils.waitForAssignment(messageListenerContainer,
          kafkaEmbedded.getPartitionsPerTopic());
    }
  }

  @Test
  void Should_ReceiveMessage_WrittenOnTopic() throws Exception {

    RandomTempAvroModel avroModel = RandomTempAvroModelFixture.generateAvroModel();

//    mockSchemaRegistry(avroModel.getSchema());

    sendToKafkaAndWait(kafkaConfigData.getTopicName(), avroModel);

    Awaitility
        .waitAtMost(10, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          assertAvroModel(avroModel);
        });
    assertThat(true).isTrue();
  }

  private void mockSchemaRegistry(Schema schema) throws RestClientException, IOException {
    when(schemaRegistryClient.getSchemaBySubjectAndId(any(), anyInt())).thenReturn(new AvroSchema(schema));
  }

  private void assertAvroModel(RandomTempAvroModel avroModel) {

    log.debug("comparing sent={} vs received={}", avroModel, RandomTempKafkaConsumer.lastAvroModel);

    RandomTempAvroModel lastAvroModel = RandomTempKafkaConsumer.lastAvroModel;
    assertThat(avroModel.getTemp()).isEqualTo(lastAvroModel.getTemp());
    assertThat(avroModel.getCreatedAt()).isEqualTo(lastAvroModel.getCreatedAt());
    assertThat(avroModel.getId()).isEqualTo(lastAvroModel.getId());
    assertThat(avroModel.getDeviceId()).isEqualTo(lastAvroModel.getDeviceId());
  }
}