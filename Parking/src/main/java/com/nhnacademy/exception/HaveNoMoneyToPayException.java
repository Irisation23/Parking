package com.nhnacademy.exception;

public class HaveNoMoneyToPayException extends IllegalArgumentException{
    public HaveNoMoneyToPayException(){
        super("HaveNoMoneyToPay");
    }
}
