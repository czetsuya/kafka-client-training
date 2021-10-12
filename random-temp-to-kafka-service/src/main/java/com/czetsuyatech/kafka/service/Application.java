package com.czetsuyatech.kafka.service;

import com.czetsuyatech.kafka.service.init.StreamInitializer;
import com.czetsuyatech.kafka.service.runner.StreamRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.czetsuyatech")
public class Application implements CommandLineRunner {

  private StreamInitializer streamInitializer;
  private StreamRunner streamRunner;

  public Application(StreamInitializer streamInitializer, StreamRunner streamRunner) {

    this.streamInitializer = streamInitializer;
    this.streamRunner = streamRunner;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    streamInitializer.init();
    streamRunner.start();
  }
}
