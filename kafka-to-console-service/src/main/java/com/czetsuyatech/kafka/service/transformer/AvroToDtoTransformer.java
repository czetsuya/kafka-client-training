package com.czetsuyatech.kafka.service.transformer;

import com.czetsuyatech.kafka.avro.model.RandomTempAvroModel;
import com.czetsuyatech.kafka.service.dto.TempDto;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AvroToDtoTransformer {

  public List<TempDto> getTempModels(List<RandomTempAvroModel> avroModels) {
    return avroModels.stream()
        .map(avroModel -> TempDto
            .builder()
            .deviceId(avroModel.getDeviceId())
            .id(avroModel.getId())
            .temp(new BigDecimal(new BigInteger(avroModel.getTemp().array()), 1))
            .createdAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(avroModel.getCreatedAt().longValue()),
                ZoneId.systemDefault()))
            .build()
        ).collect(Collectors.toList());
  }
}
