package dojo.bank.kata;

import dojo.bank.kata.repositories.InMemoryTransactionRepository;
import dojo.bank.kata.services.DefaultAccountService;
import dojo.bank.kata.services.Display;
import dojo.bank.kata.services.Time;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class AcceptanceTest {

  @Mock
  private Display display;
  @Mock
  private Time time;

  @Test
  void example_scenario_works() {
    given(time.now())
        .willReturn(LocalDateTime.parse("2012-01-10T00:00:00"))
        .willReturn(LocalDateTime.parse("2012-01-13T00:00:00"))
        .willReturn(LocalDateTime.parse("2012-01-14T00:00:00"));

    var account = new DefaultAccountService(display, new InMemoryTransactionRepository(), time);
    account.deposit(1000);
    account.deposit(2000);
    account.withdraw(500);

    account.printStatement();

    var inOrder = inOrder(display);
    inOrder.verify(display).show("Date       || Amount || Balance");
    inOrder.verify(display).show("14/01/2012 || -500   || 2500");
    inOrder.verify(display).show("13/01/2012 || 2000   || 3000");
    inOrder.verify(display).show("10/01/2012 || 1000   || 1000");
  }
}
