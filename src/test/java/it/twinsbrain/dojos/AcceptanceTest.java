package it.twinsbrain.dojos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import it.twinsbrain.dojos.domain.Balance;
import it.twinsbrain.dojos.domain.InMemoryAccount;
import it.twinsbrain.dojos.fixtures.InMemoryDisplay;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import org.junit.jupiter.api.Test;

public class AcceptanceTest {

  @Test
  void itWorks() {
    var time =
        new FixedMomentsTime(
            LocalDateTime.parse("2012-01-10T00:00:00.000"),
            LocalDateTime.parse("2012-01-13T00:00:00.000"),
            LocalDateTime.parse("2012-01-14T00:00:00.000"));
    var display = new InMemoryDisplay();
    var accountService = new InMemoryAccount(new Balance(), time, display);

    accountService.deposit(1000);
    accountService.deposit(2000);
    accountService.withdraw(500);

    accountService.printStatement();

    assertThat(
        display.messages(),
        contains(
            "Date       || Amount || Balance",
            "14/01/2012 || -500   || 2500",
            "13/01/2012 || 2000   || 3000",
            "10/01/2012 || 1000   || 1000"));
  }

  private static class FixedMomentsTime implements Time {
    private final Queue<LocalDateTime> queue;

    private FixedMomentsTime(LocalDateTime... moments) {
      this.queue = new LinkedList<>();
      queue.addAll(Arrays.asList(moments));
    }

    @Override
    public LocalDateTime now() {
      return queue.remove();
    }
  }
}
