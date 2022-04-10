package com.nhnacademy.exception;

public class TruckOutException extends IllegalArgumentException{
    public TruckOutException(){
        super("Trucks are not allowed here");
    }
}
