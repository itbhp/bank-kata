package it.twinsbrain.dojos;

import it.twinsbrain.dojos.model.Balance;
import it.twinsbrain.dojos.model.Transaction;

import java.util.List;

public interface StatementPrinter {

    void printStatement(Balance balance, List<Transaction> transactionList);
}
