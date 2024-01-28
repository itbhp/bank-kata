package it.twinsbrain.dojos.adapters;

import it.twinsbrain.dojos.Display;
import it.twinsbrain.dojos.domain.Balance;
import it.twinsbrain.dojos.domain.Deposit;
import it.twinsbrain.dojos.domain.StatementPrinter;
import it.twinsbrain.dojos.domain.Transaction;
import it.twinsbrain.dojos.domain.Withdraw;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class OnlyTextStatementPrinter implements StatementPrinter {
  private final Display display;

  public OnlyTextStatementPrinter(Display display) {
    this.display = display;
  }

  @Override
  public void printStatement(Balance balance, List<Transaction> transactionList) {
    display.show("Date       || Amount || Balance");
    var balanceReversedQueue = new LinkedList<Integer>();
    balanceReversedQueue.add(balance.value());
    transactionList.stream()
        .sorted(Comparator.comparing(Transaction::time).reversed())
        .peek(
            m -> {
              var current = balanceReversedQueue.getLast();
              var previousBalance =
                  switch (m) {
                    case Deposit deposit -> current - deposit.amount();
                    case Withdraw withdraw -> current + withdraw.amount();
                  };
              balanceReversedQueue.add(previousBalance);
            })
        .forEach(
            transaction -> {
              var amount =
                  switch (transaction) {
                    case Deposit deposit -> String.valueOf(deposit.amount());
                    case Withdraw withdraw -> "-" + withdraw.amount();
                  };
              var message =
                  padRight(FORMATTER.format(transaction.time()), 11)
                      + "|| "
                      + padRight(amount, 7)
                      + "|| "
                      + balanceReversedQueue.pollFirst();
              display.show(message);
            });
  }

  private static String padRight(String s, int n) {
    return String.format("%-" + n + "s", s);
  }

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
