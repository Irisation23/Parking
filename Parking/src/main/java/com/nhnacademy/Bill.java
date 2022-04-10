package com.nhnacademy;

public class Bill {
    Car car;
    ParkingSpaces parkingSpaces;
    ParkingTime parkingTime;

    public Bill(Car car, ParkingSpaces parkingSpaces, ParkingTime parkingTime) {
        this.car = car;
        this.parkingSpaces = parkingSpaces;
        this.parkingTime = parkingTime;
    }

    //TODO 결제할 돈이 각 시간에 맞게.
    public int willPayToMoney() {
        return 1000;
    }
}
