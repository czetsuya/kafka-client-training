package com.czetsuyatech.kafka.admin.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "random-words-to-kafka-service")
public class RandomWordsToKafkaConfigData {

  private List<String> randomWords;
  private String welcomeMessage;
  private Boolean enableMockTweets;
  private Long mockSleepMs;
  private Integer mockMinTweetLength;
  private Integer mockMaxTweetLength;
}
