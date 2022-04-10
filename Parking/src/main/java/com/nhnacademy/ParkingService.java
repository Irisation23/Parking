package com.nhnacademy;

import com.nhnacademy.exception.EnableParkingException;
import java.time.LocalDateTime;

public class ParkingService {
    CarRepository carRepository;

    public ParkingService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public ParkingTime parkingCar(Car car, ParkingSpaces parkingSpaces) {
        if(parkingSpaces.isCanPark()){
            parkingSpaces.closedSpace();
            parkingSpaces.setStartParkingTime(LocalDateTime.now());
            return new ParkingTime(car, parkingSpaces);
        }else {
            throw new EnableParkingException();
        }
    }

    public Bill outOfCarSpace(Car car, ParkingSpaces parkingSpaces ,ParkingTime parkingTime) {
        parkingSpaces.setEndParkingTime(LocalDateTime.now());
        return new Bill(car, parkingSpaces, parkingTime);
    }




}
