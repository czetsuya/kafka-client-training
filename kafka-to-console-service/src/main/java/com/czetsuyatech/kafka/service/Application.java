package com.czetsuyatech.kafka.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.czetsuyatech")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
