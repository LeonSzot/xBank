package com.example.xbank;

import java.util.Date;

public class Transaction {
    private int cardNumber;
    private Date date;
    private double amount;
    private String transactionType;

    public Transaction(Date date, int cardNumber, double amount, String transactionType) {
        this.date = date;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.transactionType = transactionType;

    }

    public int getCardNumber() {
        return cardNumber;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }
}
