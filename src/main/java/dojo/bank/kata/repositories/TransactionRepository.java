package dojo.bank.kata.repositories;

import java.util.List;

import dojo.bank.kata.model.Transaction;


public interface TransactionRepository {
  void recordTransaction(Transaction transaction);

  List<Transaction> allTransactions();
}
