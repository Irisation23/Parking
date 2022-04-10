package com.nhnacademy;

import java.time.LocalDateTime;

public class ParkingSpaces {
    private String code;
    private boolean canPark;

    LocalDateTime startParkingTime;
    LocalDateTime endParkingTime;

    public void setStartParkingTime(LocalDateTime startParkingTime) {
        this.startParkingTime = startParkingTime;
    }

    public void setEndParkingTime(LocalDateTime endParkingTime) {
        this.endParkingTime = endParkingTime;
    }


    public ParkingSpaces(String code, boolean canPark) {
        this.code = code;
        this.canPark = canPark;
    }




    public boolean isCanPark() {
        return canPark;
    }

    public void closedSpace(){
        this.canPark = false;
    }

    public LocalDateTime getStartParkingTime() {
        return this.startParkingTime;
    }

    public LocalDateTime getEndParkingTime() {
        return this.endParkingTime;
    }
}
