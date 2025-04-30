package com.example.xbank;

import java.util.Date;

public class Card {
    private int cardNumber;
    private int cvv;
    private Date date;
    private String name;
    private String surname;

    public Card(int cardNumber, int cvv, Date date, String name, String surname) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.date = date;
        this.name = name;
        this.surname = surname;
    }

    public Date getDate() {
        return date;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
