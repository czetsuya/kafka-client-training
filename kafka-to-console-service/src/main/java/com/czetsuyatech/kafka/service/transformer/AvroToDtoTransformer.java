package com.czetsuyatech.kafka.service.transformer;

import com.czetsuyatech.kafka.avro.model.RandomWordAvroModel;
import com.czetsuyatech.kafka.service.dto.WordDto;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AvroToDtoTransformer {

  public List<WordDto> getWordModels(List<RandomWordAvroModel> avroModels) {
    return avroModels.stream()
        .map(avroModel -> WordDto
            .builder()
            .userId(avroModel.getUserId())
            .id(avroModel.getId())
            .word(avroModel.getWord())
            .createdAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(avroModel.getCreatedAt().longValue()),
                ZoneId.systemDefault()))
            .build()
        ).collect(Collectors.toList());
  }
}
