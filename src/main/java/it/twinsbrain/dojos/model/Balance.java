package it.twinsbrain.dojos.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Balance {

  private final AtomicInteger value = new AtomicInteger(0);

  public void increaseBy(int amount) {
    value.addAndGet(amount);
  }

  public void decreaseBy(int amount) {
    value.addAndGet(-amount);
  }

  public int value() {
    return value.get();
  }
}
