package com.funix.fx21990.asm4.iface;

import com.funix.fx21990.asm4.model.Account;
import com.funix.fx21990.asm4.model.Transation;

public interface IReport {
    void IReport(double amount, Transation type, Account receiveAccount);
}
