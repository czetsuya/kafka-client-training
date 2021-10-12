package com.czetsuyatech.kafka.service.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempDto {

  private Long id;
  private Long deviceId;
  private Date createdAt;
  private BigDecimal temp;
}
