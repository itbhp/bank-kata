package it.twinsbrain.dojos.fixtures;

import it.twinsbrain.dojos.Time;
import java.time.LocalDateTime;
import java.util.Objects;
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

  public static UpdatingTime fixedTimeAt(LocalDateTime time) {
    return new Builder().withInitialTime(time).build();
  }

  public static class Builder {
    private LocalDateTime time;
    private UnaryOperator<LocalDateTime> nextTime;

    public static Builder anUpdatingTime() {
      return new Builder();
    }

    public Builder withInitialTime(LocalDateTime time) {
      this.time = time;
      return this;
    }

    public Builder withNextTimeProvidedBy(UnaryOperator<LocalDateTime> function) {
      this.nextTime = function;
      return this;
    }

    public UpdatingTime build() {
      Objects.requireNonNull(time);
      if (nextTime == null) {
        nextTime = UnaryOperator.identity();
      }
      return new UpdatingTime(time, nextTime);
    }
  }
}
