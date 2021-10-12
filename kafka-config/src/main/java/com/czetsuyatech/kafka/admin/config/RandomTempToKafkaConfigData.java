package com.czetsuyatech.kafka.admin.config;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "random-temp-to-kafka-service")
public class RandomTempToKafkaConfigData {

  private List<BigDecimal> randomTemps;
  private String welcomeMessage;
  private Boolean enableMockTweets;
  private Long mockSleepMs;
  private Integer mockMinTweetLength;
  private Integer mockMaxTweetLength;
}
