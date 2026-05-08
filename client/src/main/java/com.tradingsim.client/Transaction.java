package com.tradingsim.client;

public class Transaction {

    private String type;
    private double amount;
    private double price;
    private String date;

    public Transaction(String type, double amount, double price, String date) {
        this.type = type;
        this.amount = amount;
        this.price = price;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}