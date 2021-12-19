package com.czetsuyatech.kafka.service.consumer;

import com.czetsuyatech.kafka.admin.config.KafkaConfigData;
import com.czetsuyatech.kafka.admin.config.KafkaProducerConfigData;
import com.czetsuyatech.kafka.avro.model.RandomTempAvroModel;
import com.czetsuyatech.kafka.service.Application;
import com.czetsuyatech.kafka.service.consumer.impl.MockKafkaAvroSerializer;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@ActiveProfiles("test-kafka")
@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {Application.class})
@Tag("integration-test")
@Slf4j
@DirtiesContext
@EmbeddedKafka(
    partitions = 1,
    controlledShutdown = false,
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"},
    topics = {
        "${kafka-config.topic-name}"
    }
)
@TestInstance(Lifecycle.PER_CLASS)
public abstract class IntegrationTest {

  @Autowired
  protected KafkaConfigData kafkaConfigData;

  @Autowired
  protected KafkaTemplate<Long, RandomTempAvroModel> kafkaTemplate;

  @Autowired
  protected EmbeddedKafkaBroker kafkaEmbedded;

  @Autowired
  protected KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  protected void sendToKafkaAndWait(final String topic, final RandomTempAvroModel value) throws Exception {

    log.debug("write message={} to topic={}", value, topic);

    ListenableFuture<SendResult<Long, RandomTempAvroModel>> future = kafkaTemplate.send(topic, value.getId(), value);

    try {
      future.get();

    } catch (Exception e) {
      throw new Exception("Failed sending Kafka message with error=" + e.getMessage());
    }
  }

  static class MockRegistryConfig {

    @Bean
    public MockSchemaRegistryClient mockSchemaRegistryClient() {
      return new MockSchemaRegistryClient();
    }
  }

  static class Config {

    @Autowired
    protected KafkaProducerConfigData kafkaProducerConfigData;

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<Serializable, SpecificRecordBase> kafkaProducerFactory() {

      final Map<String, Object> configProps = new HashMap<>();
      configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
      configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
      configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MockKafkaAvroSerializer.class);
      configProps.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
      configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCompressionType());
      configProps.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAcks());
      configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.getRequestTimeoutMs());
      configProps.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());
      configProps.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

      return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Serializable, SpecificRecordBase> kafkaTemplateFactory() {
      return new KafkaTemplate<>(kafkaProducerFactory());
    }
  }
}
