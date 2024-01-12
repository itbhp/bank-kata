package it.twinsbrain.dojos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

  private final LocalDateTime initialTime = LocalDateTime.parse("2024-01-12T12:30:00.000");
  private final Time fiveMinPacedTime = new FiveMinutesPacedTime(initialTime);

  @Test
  void deposit_increase_balance() {
    var balance = new Balance(0);
    var account = new InMemoryAccountService(balance, fiveMinPacedTime);

    account.deposit(10);

    assertThat(balance.value(), equalTo(10));
  }

  @Test
  void withdraw_decrease_balance() {
    var balance = new Balance(10);
    var account = new InMemoryAccountService(balance, fiveMinPacedTime);

    account.withdraw(3);

    assertThat(balance.value(), equalTo(7));
  }

  @Test
  @Disabled
  void print_statement_after_one_deposit() {
    var balance = new Balance(10);
    var account = new InMemoryAccountService(balance, fiveMinPacedTime);

    account.deposit(3);

    account.printStatement();
  }
}
