package it.twinsbrain.dojos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import it.twinsbrain.dojos.model.Balance;
import java.time.LocalDateTime;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

  private final Time time =
      new UpdatingTime(LocalDateTime.parse("1970-01-12T12:30:00.000"), UnaryOperator.identity());
  private final InMemoryDisplay display = new InMemoryDisplay();

  @Test
  void deposit_increases_balance() {
    var balance = new Balance();
    var account = new InMemoryAccountService(balance, time, display);

    account.deposit(10);

    assertThat(balance.value(), equalTo(10));
  }

  @Test
  void withdraw_decreases_balance() {
    var balance = new Balance();
    var account = new InMemoryAccountService(balance, time, display);

    account.deposit(10);
    account.withdraw(3);

    assertThat(balance.value(), equalTo(7));
  }

  @Test
  void print_statement_after_one_deposit_works_fine() {
    var balance = new Balance();
    var account =
        new InMemoryAccountService(
            balance,
            new UpdatingTime(
                LocalDateTime.parse("2024-01-13T17:50:00.000"), UnaryOperator.identity()),
            display);

    account.deposit(3);

    account.printStatement();

    assertThat(
        display.messages(),
        contains("Date       || Amount || Balance", "13/01/2024 || 3      || 3"));
  }

  @Test
  void print_statement_after_one_withdraw_works_fine() {
    var balance = new Balance();
    var account =
        new InMemoryAccountService(
            balance,
            new UpdatingTime(
                LocalDateTime.parse("2024-01-13T17:50:00.000"), UnaryOperator.identity()),
            display);

    account.withdraw(3);

    account.printStatement();

    assertThat(
        display.messages(),
        contains("Date       || Amount || Balance", "13/01/2024 || -3     || -3"));
  }

  @Test
  void print_movements_from_most_recent_or_in_reverse_chronological_order() {
    var balance = new Balance();
    var account =
        new InMemoryAccountService(
            balance,
            new UpdatingTime(
                LocalDateTime.parse("2024-01-13T17:50:00.000"),
                previousTime -> previousTime.plusDays(1)),
            display);

    account.deposit(25);
    account.withdraw(3);
    account.withdraw(2);

    account.printStatement();

    assertThat(
        display.messages(),
        contains(
            "Date       || Amount || Balance",
            "15/01/2024 || -2     || 20",
            "14/01/2024 || -3     || 22",
            "13/01/2024 || 25     || 25"));
  }
}
