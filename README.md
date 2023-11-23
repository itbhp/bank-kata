# Bank Kata

## Instructions

You have an interface

```java
public interface AccountService
{
    void deposit(int amount) 
    void withdraw(int amount) 
    void printStatement()
}
```

You have to write a class Account that implements this interface.

## Rules

You cannot change the given interface

## Acceptance test

    If a client deposit 1000 on 10-01-2012
    and then he deposit 2000 on 13-01-2012
    and then he withdraw 500 on 14-01-2012
    When he ask for his bank statement, he should see:


```
Date       || Amount || Balance
14/01/2012 || -500   || 2500
13/01/2012 || 2000   || 3000
10/01/2012 || 1000   || 1000
```
