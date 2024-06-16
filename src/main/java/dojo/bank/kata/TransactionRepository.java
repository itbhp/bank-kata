package dojo.bank.kata;

import java.util.List;


public interface TransactionRepository {
    void recordTransaction(Transaction transaction);
    List<Transaction> allTransactions();
}
