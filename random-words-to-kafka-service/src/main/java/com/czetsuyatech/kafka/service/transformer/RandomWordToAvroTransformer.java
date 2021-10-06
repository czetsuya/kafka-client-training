package com.czetsuyatech.kafka.service.transformer;

import com.czetsuyatech.kafka.avro.model.RandomWordAvroModel;
import com.czetsuyatech.kafka.service.dto.WordDto;
import org.springframework.stereotype.Component;

@Component
public class RandomWordToAvroTransformer {

  public RandomWordAvroModel getRandomAvroModelFromDto(WordDto wordDto) {

    return RandomWordAvroModel
        .newBuilder()
        .setId(wordDto.getId())
        .setWord(wordDto.getWord())
        .setCreatedAt(wordDto.getCreatedAt().getTime())
        .setUserId(wordDto.getUserId())
        .build();
  }
}
