package com.example.xbank;

public class AccountData {
    private double balance;
    private String accountNumber;
    private String accountType;

    public AccountData(double balance, String accountNumber, String accountType) {
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
