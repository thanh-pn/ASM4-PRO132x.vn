package com.funix.fx21990.asm4.iface;

import com.funix.fx21990.asm4.model.Account;

import java.io.IOException;

public interface ITrasfer {
    //xử lý nghiệp vụ chuyển tiền
    void transfer(Account receiveAccount, double amount) throws IOException;
}
