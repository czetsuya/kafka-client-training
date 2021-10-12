package com.czetsuyatech.kafka.service.exception;

public class RandomTempToKafkaException extends RuntimeException {

  public RandomTempToKafkaException() {
    super();
  }

  public RandomTempToKafkaException(String message) {
    super(message);
  }

  public RandomTempToKafkaException(String message, Throwable cause) {
    super(message, cause);
  }
}
