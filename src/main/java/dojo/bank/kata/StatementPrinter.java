package dojo.bank.kata;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;


public class StatementPrinter {

    private final TransactionRepository transactionRepository;

    public StatementPrinter(TransactionRepository repository) {
        this.transactionRepository = repository;
    }

    public void printOn(Display display) {
        display.show("Date       || Amount || Balance");
        AtomicInteger balance = new AtomicInteger(0);
        transactionRepository.allTransactions().stream()
            .sorted(Comparator.comparing(Transaction::timestamp))
            .map(transaction -> {
                var transactionAmount = switch (transaction) {
                    case Withdrawal t -> {
                        balance.addAndGet(-t.amount());
                        yield -t.amount();
                    }
                    case Deposit t -> {
                        balance.addAndGet(t.amount());
                        yield t.amount();
                    }
                    default -> throw new IllegalArgumentException("Unknown transaction type");
                };
                var transactionTimestamp = transaction.timestamp();
                return new StatementLine(
                    transactionTimestamp,
                    String.format(
                        "%s || %s || %d",
                        padRight(FORMATTER.format(transactionTimestamp.toLocalDate()), 10),
                        padRight(String.valueOf(transactionAmount), 6),
                        balance.get()
                    )
                );
            })
            .sorted(Comparator.comparing(StatementLine::time).reversed())
            .map(StatementLine::msg)
            .forEach(display::show);
    }

    private String padRight(String s, int n) {  // padRight method
        return String.format("%-" + n + "s", s);
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    private record StatementLine(LocalDateTime time, String msg) {

    }
}
