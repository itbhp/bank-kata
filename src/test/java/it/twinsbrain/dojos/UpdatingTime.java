package it.twinsbrain.dojos;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

public class UpdatingTime implements Time {

  private final AtomicReference<LocalDateTime> time;
  private final UnaryOperator<LocalDateTime> nextTime;

  public UpdatingTime(LocalDateTime initialTime, UnaryOperator<LocalDateTime> updateTime) {
    this.time = new AtomicReference<>(initialTime);
    this.nextTime = updateTime;
  }

  @Override
  public LocalDateTime now() {
    return time.getAndUpdate(nextTime);
  }
}
