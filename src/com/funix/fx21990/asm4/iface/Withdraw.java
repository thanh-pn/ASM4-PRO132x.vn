package com.funix.fx21990.asm4.iface;

import java.io.IOException;

public interface Withdraw {
    boolean withdraw(double amount) throws IOException;

    boolean isAccepted(double amount);
}
