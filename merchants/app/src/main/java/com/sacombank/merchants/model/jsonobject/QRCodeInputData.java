package com.sacombank.merchants.model.jsonobject;

/**
 * Created by DUY on 8/16/2017.
 */

public class QRCodeInputData {
    String amount;
    String tip;
    String bill;
    String description;

    public QRCodeInputData(String amount, String tip, String bill, String description) {
        this.amount = amount;
        this.tip = tip;
        this.bill = bill;
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
