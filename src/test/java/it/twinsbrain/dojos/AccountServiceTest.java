package it.twinsbrain.dojos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

  private final Time fiveMinPacedTime = new FiveMinutesPacedTime(
          LocalDateTime.parse("2024-01-12T12:30:00.000")
  );
  private final InMemoryDisplay display = new InMemoryDisplay();

  @Test
  void deposit_increase_balance() {
    var balance = new Balance(0);
    var account = new InMemoryAccountService(balance, fiveMinPacedTime, display);

    account.deposit(10);

    assertThat(balance.value(), equalTo(10));
  }

  @Test
  void withdraw_decrease_balance() {
    var balance = new Balance(10);
    var account = new InMemoryAccountService(balance, fiveMinPacedTime, display);

    account.withdraw(3);

    assertThat(balance.value(), equalTo(7));
  }

  @Test
  void print_statement_after_one_deposit() {
    var balance = new Balance(10);
    var account = new InMemoryAccountService(
            balance,
            new FiveMinutesPacedTime(
                    LocalDateTime.parse("2024-01-13T17:50:00.000")
            ),
            display
    );

    account.deposit(3);

    account.printStatement();

    assertThat(display.messages(),
            contains(
                    "Date       || Amount || Balance",
                    "13/01/2024 || 3      || 13"
            )
    );
  }
}
