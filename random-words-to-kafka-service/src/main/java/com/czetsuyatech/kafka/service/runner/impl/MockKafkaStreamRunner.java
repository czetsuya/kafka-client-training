package com.czetsuyatech.kafka.service.runner.impl;

import com.czetsuyatech.kafka.admin.config.RandomWordsToKafkaConfigData;
import com.czetsuyatech.kafka.service.dto.WordDto;
import com.czetsuyatech.kafka.service.exception.RandomWordsToKafkaException;
import com.czetsuyatech.kafka.service.listener.RandomWordsKafkaStatusListener;
import com.czetsuyatech.kafka.service.runner.StreamRunner;
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

  private RandomWordsToKafkaConfigData randomWordsToKafkaConfigData;
  private RandomWordsKafkaStatusListener randomWordsKafkaStatusListener;

  public MockKafkaStreamRunner(RandomWordsToKafkaConfigData randomWordsToKafkaConfigData,
      RandomWordsKafkaStatusListener randomWordsKafkaStatusListener) {
    this.randomWordsToKafkaConfigData = randomWordsToKafkaConfigData;
    this.randomWordsKafkaStatusListener = randomWordsKafkaStatusListener;
  }

  @Override
  public void start() {

    final String[] words = randomWordsToKafkaConfigData.getRandomWords().toArray(new String[0]);
    long sleepTimeMs = randomWordsToKafkaConfigData.getMockSleepMs();
    LOG.info("Starting mock filtering random words streams {}", Arrays.toString(words));
    simulateRandomWordsStream(words, sleepTimeMs);
  }

  private void simulateRandomWordsStream(String[] words, long sleepTimeMs) {

    Executors.newSingleThreadExecutor().submit(() -> {
      while (true) {
        String word = words[RANDOM.nextInt(words.length)];
        WordDto wordDto = createWordModel(word);
        randomWordsKafkaStatusListener.onStatus(wordDto);
        sleep(sleepTimeMs);
      }
    });
  }

  private WordDto createWordModel(String word) {

    return WordDto.builder()
        .word(word)
        .createdAt(new Date())
        .id(RANDOM.nextLong())
        .userId(RANDOM.nextLong())
        .build();
  }

  private void sleep(long sleepTimeMs) {

    try {
      Thread.sleep(sleepTimeMs);

    } catch (InterruptedException e) {
      throw new RandomWordsToKafkaException(e.getMessage());
    }
  }
}
