package com.czetsuyatech.kafka.service.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WordDto {

  private Long id;
  private Long userId;
  private LocalDateTime createdAt;
  private String word;
}
