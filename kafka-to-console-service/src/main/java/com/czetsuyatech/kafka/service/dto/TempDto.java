package com.czetsuyatech.kafka.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempDto {

  private Long id;
  private Long deviceId;
  private LocalDateTime createdAt;
  private BigDecimal temp;
}
