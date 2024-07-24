package agh.project.databaseService;

import agh.project.databaseModel.Car;
import agh.project.databaseRepository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class CarServiceTests {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, "ABC123", 100, "Sedan", "Available", "Good car", null, 500, null);
        Car car2 = new Car("2", "Honda", "Civic", 2019, "XYZ789", 120, "Sedan", "Rented", "Reliable", null, 600, null);
        when(carRepository.findAll()).thenReturn(List.of(car1, car2));

        List<Car> cars = carService.findAll();

        assertThat(cars).hasSize(2);
        assertThat(cars).containsExactlyInAnyOrder(car1, car2);
    }

    @Test
    public void testFindById_Exists() {
        Car car = new Car("1", "Toyota", "Corolla", 2020, "ABC123", 100, "Sedan", "Available", "Good car", null, 500, null);
        when(carRepository.findBy_id("1")).thenReturn(Optional.of(car));

        Car foundCar = carService.findByID("1");

        assertThat(foundCar).isEqualTo(car);
    }

    @Test
    public void testFindById_NotExists() {
        when(carRepository.findBy_id("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carService.findByID("1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Car not exist");
    }

    @Test
    public void testFindByBrand() {
        Car car = new Car("1", "Toyota", "Corolla", 2020, "ABC123", 100, "Sedan", "Available", "Good car", null, 500, null);
        when(carRepository.findByBrand("Toyota")).thenReturn(List.of(car));

        List<Car> cars = carService.findByBrand("Toyota");

        assertThat(cars).containsExactly(car);
    }

    @Test
    public void testFindByPriceLessThan() {
        Car car = new Car("1", "Toyota", "Corolla", 2020, "ABC123", 90, "Sedan", "Available", "Good car", null, 500, null);
        when(carRepository.findByPriceLessThan(100)).thenReturn(List.of(car));

        List<Car> cars = carService.findByPriceLessThan(100);

        assertThat(cars).containsExactly(car);
    }

    @Test
    public void testFindByPriceGreaterThan() {
        Car car = new Car("1", "Toyota", "Corolla", 2020, "ABC123", 150, "Sedan", "Available", "Good car", null, 500, null);
        when(carRepository.findByPriceGreaterThan(120)).thenReturn(List.of(car));

        List<Car> cars = carService.findByPriceGreaterThan(120);

        assertThat(cars).containsExactly(car);
    }

    @Test
    public void testFindByPriceBetween() {
        Car car = new Car("1", "Toyota", "Corolla", 2020, "ABC123", 110, "Sedan", "Available", "Good car", null, 500, null);
        when(carRepository.findByPriceBetween(100, 120)).thenReturn(List.of(car));

        List<Car> cars = carService.findByPriceBetween(100, 120);

        assertThat(cars).containsExactly(car);
    }

    @Test
    public void testFindByAvailability() {
        Car car = new Car("1", "Toyota", "Corolla", 2020, "ABC123", 100, "Sedan", "Available", "Good car", null, 500, null);
        when(carRepository.findByAvailability("Available")).thenReturn(List.of(car));

        List<Car> cars = carService.findByAvailability("Available");

        assertThat(cars).containsExactly(car);
    }
}
