package dojo.bank.kata.repositories;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import dojo.bank.kata.model.Deposit;
import dojo.bank.kata.model.Transaction;
import dojo.bank.kata.model.Withdrawal;


abstract class TransactionRepositoryContractTest {

  @Nested
  class RegisterTransaction {

    @Test
    void recordDeposit_should_save_it() {
      var deposit = new Deposit(
          1000,
          LocalDateTime.parse("2024-06-18T12:00:00")
      );

      repository().recordTransaction(deposit);

      assertThat(
          repository().allTransactions(),
          containsInAnyOrder(deposit)
      );
    }

    @Test
    void recordWithdrawal_should_save_it() {
      var withdrawal = new Withdrawal(
          400,
          LocalDateTime.parse("2024-07-18T12:00:00")
      );

      repository().recordTransaction(withdrawal);

      assertThat(
          repository().allTransactions(),
          containsInAnyOrder(withdrawal)
      );
    }
  }


  @Nested
  class AllTransactions {

    @Test
    void allTransactions_should_return_empty_list_when_no_transactions() {
      var allTransactions = repository().allTransactions();

      assertThat(allTransactions, empty());
    }

    @Test
    void allTransactions_should_return_all_transactions() {
      var withdrawal = new Withdrawal(
          400,
          LocalDateTime.parse("2024-07-18T12:00:00")
      );
      var deposit = new Deposit(
          1000,
          LocalDateTime.parse("2024-06-18T12:00:00")
      );
      var repository = repositoryWith(List.of(withdrawal, deposit));

      var allTransactions = repository.allTransactions();

      assertThat(
          allTransactions,
          containsInAnyOrder(deposit, withdrawal)
      );
    }
  }

  protected abstract TransactionRepository repositoryWith(List<Transaction> transactions);

  protected abstract TransactionRepository repository();
}
