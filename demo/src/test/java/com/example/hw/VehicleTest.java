package com.example.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VehicleTest {

	Car car = new Car("Ford", "Probe", 2077);
	Motorcycle moto = new Motorcycle("Tesla", "Electrobike", 2078);

	@Test
	@DisplayName("Проверить, что экземпляр объекта Car также является экземпляром транспортного средства")
	void instanceOf() {
		assertThat(car instanceof Vehicle);
	}

	@Test
	@DisplayName("Проверить, что объект Car создается с 4-мя колесами")
	void WheelsCar() {
		assertThat(car.getNumWheels()).isEqualTo(4);
	}

	@Test
	@DisplayName("Проверить, что объект Motorcycle создается с 2-мя колесами")
	void MotorcycleWheels() {
		assertThat(moto.getNumWheels()).isEqualTo(2);
	}

	@Test
	@DisplayName("Проверить, что объект Car развивает скорость 60 в режиме тестового вождения")
	void testDriveCar() {
		car.testDrive();
		assertThat(car.getSpeed()).isEqualTo(60);
	}

	@Test
	@DisplayName("Проверить, что объект Motorcycle развивает скорость 75 в режиме тестового вождения")
	void testDriveMotorcycle() {
		moto.testDrive();
		assertThat(moto.getSpeed()).isEqualTo(75);
	}

	@Test
	@DisplayName("Проверить, что в режиме парковки (сначала testDrive, потом park, т.е. эмуляция движения транспорта) машина останавливается (speed = 0)")
	void parkCar() {
		car.testDrive();
		car.park();
		assertThat(car.getSpeed()).isEqualTo(0);
	}

	@Test
	@DisplayName("Проверить, что в режиме парковки (сначала testDrive, потом park, т.е. эмуляция движения транспорта) мотоцикл останавливается (speed = 0)")
	void parkMotorcycle() {
		moto.testDrive();
		moto.park();
		assertThat(moto.getSpeed()).isEqualTo(0);
	}
}