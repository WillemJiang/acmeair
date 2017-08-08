package com.acmeair.service;

import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {

  public Object generate() {
    return java.util.UUID.randomUUID().toString();
  }
}