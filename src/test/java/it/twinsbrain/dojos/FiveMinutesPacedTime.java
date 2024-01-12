package it.twinsbrain.dojos;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class FiveMinutesPacedTime implements Time {

  private final AtomicReference<LocalDateTime> time;

  public FiveMinutesPacedTime(LocalDateTime initialTime) {
    this.time = new AtomicReference<>(initialTime);
  }

  @Override
  public LocalDateTime now() {
    return time.getAndUpdate(previousTime -> previousTime.plusMinutes(5));
  }
}
