package com.czetsuyatech.kafka.service.consumer.impl;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.Map;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
public class MockKafkaAvroSerializer extends KafkaAvroSerializer {

  public MockKafkaAvroSerializer() {
    super();
    super.schemaRegistry = new MockSchemaRegistryClient();
  }

  public MockKafkaAvroSerializer(SchemaRegistryClient client) {
    super(new MockSchemaRegistryClient());
  }

  public MockKafkaAvroSerializer(SchemaRegistryClient client, Map<String, ?> props) {
    super(new MockSchemaRegistryClient(), props);
  }
}