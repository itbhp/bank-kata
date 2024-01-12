package it.twinsbrain.dojos;

import java.util.concurrent.atomic.AtomicInteger;

public class Balance {

  private final AtomicInteger value;

  public Balance(int value) {
    this.value = new AtomicInteger(value);
  }

  public void increaseBy(int amount) {
    value.addAndGet(amount);
  }

  public int value() {
    return value.get();
  }
}
