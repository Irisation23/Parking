package com.nhnacademy;

import java.time.Duration;

public class ParkingLot {
    CarRepository carRepository;
    ParkingSpaces parkingSpaces;
    Car car;

    private static final long TEN_MINUTE_SECOND = 10 * 60;
    private static final long ONE_DAY_SECOND = 24 * 60 * 60;
    private static final long THIRTY_MINUTE_SECOND = 30 * 60;
    private static final long ONE_HOUR_SECOND = 60 * 60;

    public ParkingLot(Car car, ParkingSpaces parkingSpaces) {
        this.car = car;
        this.parkingSpaces = parkingSpaces;
    }

    public long pay(Car car) {
        long payment = 0L;
        Duration duration = Duration.between(parkingSpaces.getStartParkingTime(), parkingSpaces.getEndParkingTime());

        long secondTime = duration.getSeconds();

        long dayPaymentCnt = secondTime / ONE_DAY_SECOND;
        if (secondTime % ONE_DAY_SECOND == 0) {
            --dayPaymentCnt;
        }

        payment += 10_000L * dayPaymentCnt;
        secondTime -= ONE_DAY_SECOND * dayPaymentCnt;


        long plusPayment = 1_000L;
        secondTime -= THIRTY_MINUTE_SECOND;


        if (secondTime > 0) {
            long plusPaymentCnt = secondTime / TEN_MINUTE_SECOND + 1;
            if (secondTime % TEN_MINUTE_SECOND == 0) {
                --plusPaymentCnt;
            }

            plusPayment += 500L * plusPaymentCnt;
        }

        if (plusPayment > 10_000L) {
            plusPayment = 10_000L;
        }

        payment += plusPayment;

//        parkingSpace.setStartParkDateTime(null);
//        parkingSpace.setEndParkDateTime(null);
//        parkingSpace.openAvailable();
//        parkingSpace.exitCar();


        return payment;
    }

    public Long newPaymentPolicyPay(Car car) {

        Long payment = 0L;

        Duration duration = Duration.between(parkingSpaces.getStartParkingTime(), parkingSpaces.getEndParkingTime());

        long secondTime = duration.getSeconds();

        long dayPaymentCnt = secondTime / ONE_DAY_SECOND;
        if (secondTime % ONE_DAY_SECOND == 0) {
            -- dayPaymentCnt;
        }

        payment += 15_000L * dayPaymentCnt;
        secondTime -= ONE_DAY_SECOND * dayPaymentCnt;


        Long plusPayment = 0L;
        if (secondTime <= THIRTY_MINUTE_SECOND) {
            secondTime = 0;
        }

        if (secondTime > 0) {
            plusPayment += 1000;
            secondTime -= ONE_HOUR_SECOND;
        }


        if (secondTime > 0) {
            long plusPaymentCnt = secondTime / TEN_MINUTE_SECOND + 1;
            if (secondTime % TEN_MINUTE_SECOND == 0) {
                --plusPaymentCnt;
            }

            plusPayment += 500L * plusPaymentCnt;
        }

        if (plusPayment > 15_000L) {
            plusPayment = 15_000L;
        }

        payment += plusPayment;

        if (car.getCarType().equals(CarType.LIGHT_CAR)) {
            payment /= 2;
        }

        return payment;
    }

}
