package com.funix.fx21990.asm4.model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String customerId;
    public User() {
        super();
    }
    public User(String name, String customerId) {
        super();
        this.name = name;
        this.customerId = customerId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
