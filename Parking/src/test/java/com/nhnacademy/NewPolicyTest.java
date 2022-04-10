package com.nhnacademy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


import com.nhnacademy.exception.EnableParkingException;
import com.nhnacademy.exception.TruckOutException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NewPolicyTest {
    static ParkingService parkingService;
    static CarRepository carRepository;

    @BeforeEach
    void setUp(){
        carRepository = mock(CarRepository.class);
        parkingService = new ParkingService(carRepository);
    }

    @DisplayName(" 트럭은 입장 불가능 합니다. ")
    @Test
    void scanCarType_TruckOutException(){
        Wallet wallet = new Wallet(500);
        Customer customer =new Customer(wallet);
        Car truck = new Car(1111, customer, CarType.TURCK);

        assertThatThrownBy(truck::isNotAllowHere)
            .isInstanceOf(TruckOutException.class)
            .hasMessageContainingAll("Truck");
    }

    @DisplayName("하루가 경과하면, 10000원을 부과 합니다.")
    @Test
    void paymentTest_NextDay(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer, CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1",false);
        ParkingLot parkingLot = new ParkingLot(car,parkingSpaces);

        parkingSpaces.setStartParkingTime(LocalDateTime.now());
        parkingSpaces.setEndParkingTime(parkingSpaces.getStartParkingTime().plusDays(1));

        assertThat(parkingLot.pay(car)).isEqualTo(10_000L);
    }
}
