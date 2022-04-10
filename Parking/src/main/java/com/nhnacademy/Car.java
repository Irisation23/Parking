package com.nhnacademy;

import com.nhnacademy.exception.TruckOutException;
import java.util.Objects;

public class Car {
    int carNumber;
    Customer customer;
    CarType carType;

    public CarType getCarType() {
        return carType;
    }

    public Car(int carNumber) {
        this.carNumber = carNumber;
    }

    public Car(int carNumber,Customer customer, CarType carType) {
        this.carNumber = carNumber;
        this.customer = customer;
        this.carType = carType;
    }

    void isNotAllowHere(){
        if(this.carType == CarType.TURCK){
            throw new TruckOutException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return carNumber == car.carNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(carNumber);
    }


}
