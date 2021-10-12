package com.czetsuyatech.kafka.service.transformer;

import com.czetsuyatech.kafka.avro.model.RandomTempAvroModel;
import com.czetsuyatech.kafka.service.dto.TempDto;
import java.nio.ByteBuffer;
import org.springframework.stereotype.Component;

@Component
public class RandomTempToAvroTransformer {

  public RandomTempAvroModel getRandomAvroModelFromDto(TempDto tempDto) {

    return RandomTempAvroModel
        .newBuilder()
        .setId(tempDto.getId())
        .setTemp(ByteBuffer.wrap(tempDto.getTemp().unscaledValue().toByteArray()))
        .setCreatedAt(tempDto.getCreatedAt().getTime())
        .setDeviceId(tempDto.getDeviceId())
        .build();
  }
}
