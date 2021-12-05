package com.czetsuyatech.kafka.service.config;

import com.czetsuyatech.kafka.admin.config.RetryConfigData;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@Slf4j
@AllArgsConstructor
@Configuration
public class KafkaBackOffConfig {

  private final ConcurrentKafkaListenerContainerFactory<Object, Object> factory;
  private final RetryConfigData retryConfigData;

  @PostConstruct
  public void initKafka() {
    log.debug("Initializing backoff configuration");
    factory.setRetryTemplate(kafkaRetry(retryConfigData));
  }

  public RetryTemplate kafkaRetry(RetryConfigData retryConfigData) {
    RetryTemplate retryTemplate = new RetryTemplate();

    ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
    exponentialBackOffPolicy.setInitialInterval(retryConfigData.getInitialIntervalMs());
    exponentialBackOffPolicy.setMultiplier(retryConfigData.getMultiplier());
    exponentialBackOffPolicy.setMaxInterval(retryConfigData.getMaxIntervalMs());

    retryTemplate.setRetryPolicy(new AlwaysRetryPolicy());
    retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

    return retryTemplate;
  }
}
