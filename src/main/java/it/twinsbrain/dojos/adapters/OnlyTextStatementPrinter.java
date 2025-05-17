package it.twinsbrain.dojos.adapters;

import it.twinsbrain.dojos.Display;
import it.twinsbrain.dojos.domain.Balance;
import it.twinsbrain.dojos.domain.Deposit;
import it.twinsbrain.dojos.domain.Transaction;
import it.twinsbrain.dojos.domain.Withdraw;
import it.twinsbrain.dojos.domain.StatementPrinter;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

public class OnlyTextStatementPrinter implements StatementPrinter {
  private final Display display;

  public OnlyTextStatementPrinter(Display display) {
    this.display = display;
  }

  @Override
  public void printStatement(Balance balance, List<Transaction> transactionList) {
    display.show("Date       || Amount || Balance");
    var reversedTransactions = transactionList.stream()
        .sorted(Comparator.comparing(Transaction::time).reversed()).toList();
    var balanceReversedQueue = getBalanceForEachTransaction(balance, reversedTransactions);
    reversedTransactions
        .forEach(transaction -> {
          var amount =
              switch (transaction) {
                case Deposit deposit -> String.valueOf(deposit.amount());
                case Withdraw withdraw -> "-" + withdraw.amount();
              };
          var message =
              padRight(TIME_FORMATTER.format(transaction.time()), 11)
                  + "|| "
                  + padRight(amount, 7)
                  + "|| "
                  + balanceReversedQueue.pollFirst();
          display.show(message);
        });
  }

  private LinkedList<Integer> getBalanceForEachTransaction(Balance balance, List<Transaction> reversedTransactions) {
    var balanceReversedQueue = new LinkedList<Integer>();
    balanceReversedQueue.add(balance.value());
    reversedTransactions
        .forEach(transaction -> updateBalance(transaction, balanceReversedQueue));
    return balanceReversedQueue;
  }

  private void updateBalance(
      Transaction transaction,
      List<Integer> balanceReversedQueue
  ) {
    var current = balanceReversedQueue.getLast();
    var previousBalance =
        switch (transaction) {
          case Deposit deposit -> current - deposit.amount();
          case Withdraw withdraw -> current + withdraw.amount();
        };
    balanceReversedQueue.add(previousBalance);
  }

  private static String padRight(String s, int n) {
    return format("%-" + n + "s", s);
  }

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
