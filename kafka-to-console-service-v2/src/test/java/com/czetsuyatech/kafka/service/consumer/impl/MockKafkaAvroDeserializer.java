package com.czetsuyatech.kafka.service.consumer.impl;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import java.util.Map;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
public class MockKafkaAvroDeserializer extends KafkaAvroDeserializer {

  public MockKafkaAvroDeserializer() {
    super();
    super.schemaRegistry = new MockSchemaRegistryClient();
  }

  public MockKafkaAvroDeserializer(SchemaRegistryClient client) {
    super(new MockSchemaRegistryClient());
  }

  public MockKafkaAvroDeserializer(SchemaRegistryClient client, Map<String, ?> props) {
    super(new MockSchemaRegistryClient(), props);
  }
}