package dojo.bank.kata.repositories;

import java.util.List;

import dojo.bank.kata.model.Transaction;


public class InMemoryTransactionRepositoryTest extends TransactionRepositoryContractTest {

  private final InMemoryTransactionRepository inMemoryTransactionRepository =
      new InMemoryTransactionRepository();

  @Override
  protected TransactionRepository repositoryWith(List<Transaction> transactions) {
    return new InMemoryTransactionRepository(transactions);
  }

  @Override
  protected TransactionRepository repository() {
    return inMemoryTransactionRepository;
  }
}
