package dojo.bank.kata;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

import java.time.Clock;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AcceptanceTest {

  @Mock private Display display;
  @Mock private Clock clock;

  @Test
  void example_scenario_works() {
    // given
    given(clock.instant())
        .willReturn(Instant.parse("2012-01-10T00:00:00Z"))
        .willReturn(Instant.parse("2012-01-13T00:00:00Z"))
        .willReturn(Instant.parse("2012-01-14T00:00:00Z"));
    given(clock.getZone())
        .willReturn(Clock.systemUTC().getZone());

    // when
    var account = new DefaultAccountService(display, new InMemoryTransactionRepository(), clock);
    account.deposit(1000);
    account.deposit(2000);
    account.withdraw(500);

    // then
    account.printStatement();

    var inOrder = inOrder(display);
    inOrder.verify(display).show("Date       || Amount || Balance");
    inOrder.verify(display).show("14/01/2012 || -500   || 2500");
    inOrder.verify(display).show("13/01/2012 || 2000   || 3000");
    inOrder.verify(display).show("10/01/2012 || 1000   || 1000");
  }
}
