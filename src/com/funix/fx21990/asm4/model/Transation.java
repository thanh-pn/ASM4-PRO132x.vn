package com.funix.fx21990.asm4.model;

import com.funix.fx21990.asm4.utils.Utils;

import java.io.Serializable;

public class Transation implements Serializable {
    private final long serialVersionUID = 1L;
    private int id;
    private String accountNumber;
    private String time;
    private double amount;
    private boolean status;
    private String type;

    public Transation(double amount, String time, boolean status, String type, String accountNumber) {
        this.amount = amount;
        this.time = time;
        this.status = status;
        this.type = type;
        this.accountNumber = accountNumber;
    }
    //type : trả về là  DEPOSIT/WITHDRAW/TRANSFER.


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;

    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[GD]   " + accountNumber + " |      "+ type  +"  |"+ amount + "   |" + time + "| " + Utils.getAdvancedOTP(id) + Utils.getAdvancedOTP(id);
    }
}
