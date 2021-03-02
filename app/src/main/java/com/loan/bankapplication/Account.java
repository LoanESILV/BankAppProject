package com.loan.bankapplication;

public class Account {
    private int id;
    private String accountName;
    private String amount;
    private String iban;
    private String currency;

    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAmount() {
        return amount;
    }

    public String getIban() {
        return iban;
    }

    public String getCurrency() {
        return currency;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
