package dojo.bank.kata.services;

import dojo.bank.kata.model.Deposit;
import dojo.bank.kata.model.Transaction;
import dojo.bank.kata.model.Withdrawal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

record StatementLine(LocalDateTime time, String msg) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static StatementLine statementLine(Transaction transaction, AtomicInteger balance) {
        var transactionAmount = transactionAmount(transaction, balance);
        var transactionTimestamp = transaction.timestamp();
        return new StatementLine(
                transactionTimestamp,
                format(
                        "%s || %s || %d",
                        padRight(FORMATTER.format(transactionTimestamp.toLocalDate()), 10),
                        padRight(String.valueOf(transactionAmount), 6),
                        balance.get()
                )
        );
    }

    private static int transactionAmount(Transaction transaction, AtomicInteger balance) {
        return switch (transaction) {
            case Withdrawal t -> {
                balance.addAndGet(-t.amount());
                yield -t.amount();
            }
            case Deposit t -> {
                balance.addAndGet(t.amount());
                yield t.amount();
            }
        };
    }

    private static String padRight(String s, int n) {
        return format("%-" + n + "s", s);
    }
}
