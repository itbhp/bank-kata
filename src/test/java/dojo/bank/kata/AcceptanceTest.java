package dojo.bank.kata;

import static org.mockito.Mockito.verify;

import java.time.Clock;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AcceptanceTest {

  @Mock private Display display;
  @Mock private TransactionRepository transactionRepository;
  @Mock private Clock clock;

  @Test
  @Disabled
  void example_scenario_works() {
    // given
    var account = new DefaultAccountService(display, transactionRepository, clock);

    // when
    account.deposit(1000);
    account.deposit(2000);
    account.withdraw(500);

    // then
    account.printStatement();

    verify(display).show("Date       || Amount || Balance");
    verify(display).show("14/01/2012 || -500   || 2500");
    verify(display).show("13/01/2012 || 2000   || 3000");
    verify(display).show("10/01/2012 || 1000   || 1000");
  }
}
