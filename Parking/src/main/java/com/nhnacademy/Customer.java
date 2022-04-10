package com.nhnacademy;

import com.nhnacademy.exception.HaveNoMoneyToPayException;

public class Customer {
    Wallet wallet;
    long nowMoney;

    public Customer(Wallet wallet) {
        this.wallet = wallet;
    }

    public Customer(Bill bill, Wallet wallet) {
        this.nowMoney = wallet.getMoney() - bill.willPayToMoney();

        if (this.nowMoney < 0) {
            throw new HaveNoMoneyToPayException();
        }
    }


    public long getNowMoney() {
        return nowMoney;
    }
}
