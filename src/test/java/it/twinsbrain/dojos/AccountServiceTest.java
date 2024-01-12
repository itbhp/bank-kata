package it.twinsbrain.dojos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class AccountServiceTest {
  @Test
  void deposit_increase_balance() {
    var balance = new Balance(0);
    var account = new InMemoryAccountService(balance);

    account.deposit(10);

    assertThat(balance.value(), equalTo(10));
  }

  @Test
  void withdraw_decrease_balance() {
    var balance = new Balance(10);
    var account = new InMemoryAccountService(balance);

    account.withdraw(3);

    assertThat(balance.value(), equalTo(7));
  }
}
