package com.nhnacademy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


import com.nhnacademy.exception.EnableParkingException;
import com.nhnacademy.exception.HaveNoMoneyToPayException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParkingServiceTest {
    static ParkingService parkingService;
    static CarRepository carRepository;

    @BeforeEach
    void setUp(){
        carRepository = mock(CarRepository.class);
        parkingService = new ParkingService(carRepository);
    }

    @DisplayName("차 번호를 확인하는 테스트.")
    @Test
    void checkCarNumberTest(){
        int scannedNumber = 1111;
        Car car = new Car(scannedNumber);

        when(carRepository.checkCarNumber(car)).thenReturn(scannedNumber);

        int testCheckNumber = 1111;
        Car testNumberCar = new Car(testCheckNumber);
        int carNumber = carRepository.checkCarNumber(testNumberCar);
        assertThat(carNumber).isEqualTo(testCheckNumber);

        verify(carRepository).checkCarNumber(testNumberCar);
    }

    @DisplayName("A-1 주차구역에 주차시 주차시간을 알려줌. ")
    @Test
    void checkParkingSpaces(){
        Car car = mock(Car.class);
        ParkingSpaces parkingSpaces = mock(ParkingSpaces.class);

        when(parkingSpaces.isCanPark()).thenReturn(true);

        assertThat(parkingService.parkingCar(car, parkingSpaces)).isInstanceOf(ParkingTime.class);

        verify(parkingSpaces).isCanPark();
    }


    @DisplayName("A-1 주차구역에 주차 실패 ")
    @Test
    void checkParkingSpaces_throwEnableParkingException() {
        //given
        Car car = mock(Car.class);
        ParkingSpaces parkingSpaces = mock(ParkingSpaces.class);
        when(parkingSpaces.isCanPark()).thenReturn(false);

        //when
        assertThatThrownBy(() -> parkingService.parkingCar(car, parkingSpaces))
            .isInstanceOf(EnableParkingException.class)
            .hasMessageContainingAll("Enable");

    }

    @DisplayName("A-1 주차구역에서 1111인 차가 나간다. 1000원 짜리 영수증을 받는다.")
    @Test
    void outOfParkingSpacesReturnBill() {
        Car car = new Car(1111);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1", true);
        ParkingTime parkingTime = mock(ParkingTime.class);
        Bill bill = new Bill(car, parkingSpaces, parkingTime);

        assertThat(bill.willPayToMoney()).isEqualTo(1000);
    }

    @DisplayName("차가 나갈려면 주차 시간만큼 결제를 해야한다.")
    @Test
    void payToMoneyTest(){
        Car car = new Car(1111);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1", true);
        ParkingTime parkingTime = new ParkingTime(car, parkingSpaces);
        Bill bill = parkingService.outOfCarSpace(car,parkingSpaces,parkingTime);
        Wallet wallet = new Wallet(10000);
        Customer customer = new Customer(bill, wallet);

        assertThat(customer.getNowMoney()).isEqualTo(9000);

    }

    @DisplayName("돈이 없으면 나갈 수 없습니다.")
    @Test
    void noMoneyInCustomerTest_HaveNoMoneyToPay(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer,CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1", true);
        ParkingTime parkingTime = new ParkingTime(car , parkingSpaces);
        Bill bill = parkingService.outOfCarSpace(car,parkingSpaces,parkingTime);

        assertThatThrownBy(()-> new Customer(bill, wallet))
            .isInstanceOf(HaveNoMoneyToPayException.class)
            .hasMessageContainingAll("HaveNoMoney");
    }


    @DisplayName("30분이 경과하면 주차요금은 1000원을 부과 합니다.")
    @Test
    void paymentTest_30Min(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer, CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1",false);
        ParkingLot parkingLot = new ParkingLot(car,parkingSpaces);

        parkingSpaces.setStartParkingTime(LocalDateTime.now());
        parkingSpaces.setEndParkingTime(parkingSpaces.getStartParkingTime().plusMinutes(30));

        assertThat(parkingLot.pay(car)).isEqualTo(1000L);

    }

    @DisplayName("30분 1초가 경과하면 주차요금은 1500원을 부과 합니다.")
    @Test
    void paymentTest_30Min1Sec(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer, CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1",false);
        ParkingLot parkingLot = new ParkingLot(car,parkingSpaces);

        parkingSpaces.setStartParkingTime(LocalDateTime.now());
        parkingSpaces.setEndParkingTime(parkingSpaces.getStartParkingTime().plusMinutes(30).plusSeconds(1));

        assertThat(parkingLot.pay(car)).isEqualTo(1500L);

    }

    @DisplayName("40분1초가 경과하면 주차요금은 2000원을 부과 합니다.")
    @Test
    void paymentTest_40Min1Sec(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer, CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1",false);
        ParkingLot parkingLot = new ParkingLot(car,parkingSpaces);

        parkingSpaces.setStartParkingTime(LocalDateTime.now());
        parkingSpaces.setEndParkingTime(parkingSpaces.getStartParkingTime().plusMinutes(40).plusSeconds(1));

        assertThat(parkingLot.pay(car)).isEqualTo(2000L);

    }

    @DisplayName("최초 30분 주차는 무료 입니다.")
    @Test
    void newPaymentTest_30Min(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer, CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1",false);
        ParkingLot parkingLot = new ParkingLot(car,parkingSpaces);

        parkingSpaces.setStartParkingTime(LocalDateTime.now());
        parkingSpaces.setEndParkingTime(parkingSpaces.getStartParkingTime().plusMinutes(30));

        assertThat(parkingLot.newPaymentPolicyPay(car)).isZero();
    }

    @DisplayName("최초 30분 초과 ~ 1시간 은 1000원을 부과 합니다.")
    @Test
    void newPaymentTest_30MinTo1Hours(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer, CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1",false);
        ParkingLot parkingLot = new ParkingLot(car,parkingSpaces);

        parkingSpaces.setStartParkingTime(LocalDateTime.now());
        parkingSpaces.setEndParkingTime(parkingSpaces.getStartParkingTime().plusMinutes(31));

        assertThat(parkingLot.newPaymentPolicyPay(car)).isEqualTo(1000L);
    }

    @DisplayName("이 후 추가 10분이 경과시 500원 부과 1초라도 지나면 부과 됨.")
    @Test
    void newPaymentTest_1Hours1sec(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer, CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1",false);
        ParkingLot parkingLot = new ParkingLot(car,parkingSpaces);

        parkingSpaces.setStartParkingTime(LocalDateTime.now());
        parkingSpaces.setEndParkingTime(parkingSpaces.getStartParkingTime().plusMinutes(60).plusSeconds(1));

        assertThat(parkingLot.newPaymentPolicyPay(car)).isEqualTo(1500L);
    }

    @DisplayName("일일 주차 = 15_000")
    @Test
    void newPaymentTest_1Days(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer, CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1",false);
        ParkingLot parkingLot = new ParkingLot(car,parkingSpaces);

        parkingSpaces.setStartParkingTime(LocalDateTime.now());
        parkingSpaces.setEndParkingTime(parkingSpaces.getStartParkingTime().plusDays(1));

        assertThat(parkingLot.newPaymentPolicyPay(car)).isEqualTo(15000L);
    }

    @DisplayName("일일 주차 = 15_000 2일 연속 주차시 30_000")
    @Test
    void newPaymentTest_2Days(){
        Wallet wallet = new Wallet(500L);
        Customer customer = new Customer(wallet);
        Car car = new Car(1111, customer, CarType.COMMON_CAR);
        ParkingSpaces parkingSpaces = new ParkingSpaces("A-1",false);
        ParkingLot parkingLot = new ParkingLot(car,parkingSpaces);

        parkingSpaces.setStartParkingTime(LocalDateTime.now());
        parkingSpaces.setEndParkingTime(parkingSpaces.getStartParkingTime().plusDays(2));

        assertThat(parkingLot.newPaymentPolicyPay(car)).isEqualTo(30_000L);
    }
}
