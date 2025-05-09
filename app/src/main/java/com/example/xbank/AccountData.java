package com.example.xbank;

import java.util.Date;

public class AccountData {
    private double balance;
    private String accountNumber;
    private String accountType;

    public AccountData(double balance, String accountType, String accountNumber) {
        this.balance = balance;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }
}
