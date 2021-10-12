package com.czetsuyatech.kafka.service.runner.impl;

import com.czetsuyatech.kafka.admin.config.RandomTempToKafkaConfigData;
import com.czetsuyatech.kafka.service.dto.TempDto;
import com.czetsuyatech.kafka.service.exception.RandomTempToKafkaException;
import com.czetsuyatech.kafka.service.listener.RandomTempKafkaStatusListener;
import com.czetsuyatech.kafka.service.runner.StreamRunner;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MockKafkaStreamRunner implements StreamRunner {

  private static final Logger LOG = LoggerFactory.getLogger(MockKafkaStreamRunner.class);

  private static final Random RANDOM = new Random();

  private RandomTempToKafkaConfigData randomTempToKafkaConfigData;
  private RandomTempKafkaStatusListener randomTempKafkaStatusListener;

  public MockKafkaStreamRunner(RandomTempToKafkaConfigData randomTempToKafkaConfigData,
      RandomTempKafkaStatusListener randomTempKafkaStatusListener) {
    this.randomTempToKafkaConfigData = randomTempToKafkaConfigData;
    this.randomTempKafkaStatusListener = randomTempKafkaStatusListener;
  }

  @Override
  public void start() {

    final BigDecimal[] temps = randomTempToKafkaConfigData.getRandomTemps().toArray(new BigDecimal[0]);
    long sleepTimeMs = randomTempToKafkaConfigData.getMockSleepMs();
    LOG.info("Starting mock filtering random temps streams {}", Arrays.toString(temps));
    simulateRandomTempStream(temps, sleepTimeMs);
  }

  private void simulateRandomTempStream(BigDecimal[] temps, long sleepTimeMs) {

    Executors.newSingleThreadExecutor().submit(() -> {
      while (true) {
        BigDecimal temp = temps[RANDOM.nextInt(temps.length)];
        TempDto tempDto = createTempModel(temp);
        randomTempKafkaStatusListener.onStatus(tempDto);
        sleep(sleepTimeMs);
      }
    });
  }

  private TempDto createTempModel(BigDecimal temp) {

    return TempDto.builder()
        .temp(temp)
        .createdAt(new Date())
        .id(RANDOM.nextLong())
        .deviceId(RANDOM.nextLong())
        .build();
  }

  private void sleep(long sleepTimeMs) {

    try {
      Thread.sleep(sleepTimeMs);

    } catch (InterruptedException e) {
      throw new RandomTempToKafkaException(e.getMessage());
    }
  }
}
