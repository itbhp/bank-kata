package it.twinsbrain.dojos;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class OneDayPacedTime implements Time {

  private final AtomicReference<LocalDateTime> time;

  public OneDayPacedTime(LocalDateTime initialTime) {
    this.time = new AtomicReference<>(initialTime);
  }

  @Override
  public LocalDateTime now() {
    return time.getAndUpdate(previousTime -> previousTime.plusDays(1));
  }
}
