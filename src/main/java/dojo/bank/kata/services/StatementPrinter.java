package dojo.bank.kata.services;

import dojo.bank.kata.model.Transaction;
import dojo.bank.kata.repositories.TransactionRepository;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static dojo.bank.kata.services.StatementLine.statementLine;
import static java.util.Comparator.comparing;

public class StatementPrinter {

    private final TransactionRepository transactionRepository;

    public StatementPrinter(TransactionRepository repository) {
        this.transactionRepository = repository;
    }

    public void printOn(Display display) {
        display.show("Date       || Amount || Balance");
        reverseOrderStatementLinesFrom(new AtomicInteger(0))
                .forEach(display::show);
    }

    private Stream<String> reverseOrderStatementLinesFrom(AtomicInteger balance) {
        return sortedTrasactions()
                .map(transaction -> statementLine(transaction, balance))
                .sorted(comparing(StatementLine::time).reversed())
                .map(StatementLine::msg);
    }

    private Stream<Transaction> sortedTrasactions() {
        return transactionRepository.allTransactions().stream()
                .sorted(comparing(Transaction::timestamp));
    }

}
