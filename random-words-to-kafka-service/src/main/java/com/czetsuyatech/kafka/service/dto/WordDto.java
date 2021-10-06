package com.czetsuyatech.kafka.service.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WordDto {

  private Long id;
  private Long userId;
  private Date createdAt;
  private String word;
}
