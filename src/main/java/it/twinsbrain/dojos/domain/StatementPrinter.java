package it.twinsbrain.dojos.domain;


import java.util.List;

public interface StatementPrinter {

    void printStatement(Balance balance, List<Transaction> transactionList);
}
