package com.sacombank.merchants.model.staticmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DUY on 8/5/2017.
 */

public class HistoryData {
    String billNo = "#No. xxx xxx";
    String date = "24/12/2017";
    String money = "100.000.000";
    String status;

    public HistoryData() {
    }

    public HistoryData(String billNo, String date, String money, String status) {
        this.billNo = billNo;
        this.date = date;
        this.money = money;
        this.status = status;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static List<HistoryData> dummyData() {
        List<HistoryData> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            HistoryData data = new HistoryData();
            data.setStatus(String.valueOf(i%2));
            dataList.add(data);
        }

        return dataList;
    }
}
