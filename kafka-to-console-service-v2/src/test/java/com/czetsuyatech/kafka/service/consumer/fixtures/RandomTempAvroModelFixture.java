package com.czetsuyatech.kafka.service.consumer.fixtures;

import com.czetsuyatech.kafka.avro.model.RandomTempAvroModel;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
public class RandomTempAvroModelFixture {

  private static BigDecimal temp = new BigDecimal("37.1");
  private static LocalDateTime dateTime = LocalDateTime.now();

  public static RandomTempAvroModel generateAvroModel() {

    RandomTempAvroModel avroModel = new RandomTempAvroModel();
    avroModel.setTemp(ByteBuffer.wrap(temp.unscaledValue().toByteArray()));
    avroModel.setCreatedAt(dateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
    avroModel.setId(100L);
    avroModel.setDeviceId(100L);

    return avroModel;
  }
}
