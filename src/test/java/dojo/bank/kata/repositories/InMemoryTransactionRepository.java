package dojo.bank.kata.repositories;

import java.util.ArrayList;
import java.util.List;

import dojo.bank.kata.model.Transaction;


public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions;

    public InMemoryTransactionRepository() {
        this.transactions = new ArrayList<>();
    }

    public InMemoryTransactionRepository(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public void recordTransaction(Transaction transaction) {
       transactions.add(transaction);
    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }
}
