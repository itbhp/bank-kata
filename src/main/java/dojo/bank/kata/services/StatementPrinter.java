package dojo.bank.kata.services;

import dojo.bank.kata.model.Transaction;
import dojo.bank.kata.repositories.TransactionRepository;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static dojo.bank.kata.services.StatementLine.statementLine;
import static java.util.Comparator.comparing;

public record StatementPrinter(
    TransactionRepository transactionRepository,
    Display display
) {

  public void print() {
    display.show("Date       || Amount || Balance");
    reverseOrderStatementLinesFrom(new AtomicInteger(0))
        .forEach(display::show);
  }

  private Stream<String> reverseOrderStatementLinesFrom(AtomicInteger balance) {
    return sortedTransactions()
        .map(transaction -> statementLine(transaction, balance))
        .sorted(comparing(StatementLine::time).reversed())
        .map(StatementLine::msg);
  }

  private Stream<Transaction> sortedTransactions() {
    return transactionRepository.allTransactions().stream();
  }

}
