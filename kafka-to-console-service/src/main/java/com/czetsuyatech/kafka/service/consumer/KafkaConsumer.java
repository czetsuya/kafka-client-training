package com.czetsuyatech.kafka.service.consumer;

import java.io.Serializable;
import java.util.List;
import org.apache.avro.specific.SpecificRecordBase;

public interface KafkaConsumer<K extends Serializable, V extends SpecificRecordBase> {

  void receive(List<V> messages, List<Long> keys, List<Integer> partitions, List<Long> offsets);
}
