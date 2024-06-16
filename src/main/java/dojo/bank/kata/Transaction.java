package dojo.bank.kata;

import java.time.LocalDateTime;


public sealed interface Transaction permits Deposit, Withdrawal{

  int amount();
  LocalDateTime timestamp();
}
