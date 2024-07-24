package agh.project.databaseRepository;

import agh.project.databaseModel.Car;
import agh.project.databaseModel.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class CarRepositoryTests {

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        carRepository.deleteAll();

        Car car1 = new Car(
                "1", "Toyota", "Camry", 2020, "XYZ123", 50, "Sedan", "A",
                "A reliable car.", new Rating(3,5, 10), 1000, "photo1.jpg"
        );
        Car car2 = new Car(
                "2", "Honda", "Civic", 2019, "ABC456", 70, "Hatchback", "R",
                "Fuel efficient.", new Rating(3, 4, 8), 800, "photo2.jpg"
        );
        Car car3 = new Car(
                "3", "Ford", "Mustang", 2021, "LMN789", 90, "Coupe", "A",
                "Sporty and fast.", new Rating(3, 5, 9), 1500, "photo3.jpg"
        );
        carRepository.saveAll(List.of(car1, car2, car3));
    }

    @Test
    public void testFindById() {
        Optional<Car> car = carRepository.findBy_id("1");
        assertThat(car).isPresent();
        Car carEntity = car.get();
        assertThat(carEntity.getBrand()).isEqualTo("Toyota");
        assertThat(carEntity.getModel()).isEqualTo("Camry");
        assertThat(carEntity.getPricePerDay()).isEqualTo(50);
    }

    @Test
    public void testFindByBrand() {
        List<Car> cars = carRepository.findByBrand("Honda");
        assertThat(cars).hasSize(1);
        Car car = cars.get(0);
        assertThat(car.getBrand()).isEqualTo("Honda");
        assertThat(car.getModel()).isEqualTo("Civic");
    }

    @Test
    public void testFindByPriceLessThan() {
        List<Car> cars = carRepository.findByPriceLessThan(60.0);
        assertThat(cars).hasSize(1);
        Car car = cars.get(0);
        assertThat(car.getBrand()).isEqualTo("Toyota");
        assertThat(car.getPricePerDay()).isLessThan(60);
    }

    @Test
    public void testFindByPriceGreaterThan() {
        List<Car> cars = carRepository.findByPriceGreaterThan(60.0);
        assertThat(cars).hasSize(2);
        assertThat(cars.stream().map(Car::getBrand).toList()).containsExactlyInAnyOrder("Honda", "Ford");
    }

    @Test
    public void testFindByPriceBetween() {
        List<Car> cars = carRepository.findByPriceBetween(50.0, 80.0);
        assertThat(cars).hasSize(2);
        assertThat(cars.stream().map(Car::getBrand).toList()).containsExactlyInAnyOrder("Toyota", "Honda");
    }

    @Test
    public void testFindByAvailability() {
        List<Car> cars = carRepository.findByAvailability("A");
        assertThat(cars).hasSize(2);
        assertThat(cars.stream().map(Car::getBrand).toList()).containsExactlyInAnyOrder("Toyota", "Ford");
    }
}
