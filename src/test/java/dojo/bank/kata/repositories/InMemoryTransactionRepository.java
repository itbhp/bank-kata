package dojo.bank.kata.repositories;

import java.util.ArrayList;
import java.util.List;

import dojo.bank.kata.model.Transaction;


public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void recordTransaction(Transaction transaction) {
       transactions.add(transaction);
    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }
}
