package com.czetsuyatech.kafka.service.exception;

public class RandomWordsToKafkaException extends RuntimeException {

  public RandomWordsToKafkaException() {
    super();
  }

  public RandomWordsToKafkaException(String message) {
    super(message);
  }

  public RandomWordsToKafkaException(String message, Throwable cause) {
    super(message, cause);
  }
}
