package com.example.xbank;

import java.util.Date;

public class AccountData {
    private double balance;
    private int accountId;
    private int accountNumber;
    private String accountType;

    public AccountData(double balance, int accountId, String accountType, int accountNumber) {
        this.balance = balance;
        this.accountId = accountId;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }
}
