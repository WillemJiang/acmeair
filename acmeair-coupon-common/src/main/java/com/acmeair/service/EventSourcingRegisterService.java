package com.acmeair.service;

public abstract class EventSourcingRegisterService<T> {
  public abstract void addEvent(T event);
}
