package agh.project.databaseService;

import agh.project.databaseModel.Car;
import agh.project.databaseRepository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public List<Car> findAll(){
        return carRepository.findAll();
    }
    public Car findByID(int id){
        Optional<Car> optionalCar = carRepository.findByCarID(id);
        if (optionalCar.isEmpty()){
            throw new RuntimeException("Car not exist");
        }
        return optionalCar.get();
    }
    public List<Car> findByBrand(String brand){
        return carRepository.findByBrand(brand);
    }
    public List<Car> findByPriceLessThan(double price){
        return carRepository.findByPriceLessThan(price);
    }
    public List<Car> findByPriceGreaterThan(double price){
        return carRepository.findByPriceGreaterThan(price);
    }
    public List<Car> findByPriceBetween(double lowerPrice, double higherPrice){
        return carRepository.findByPriceBetween(lowerPrice, higherPrice);
    }
    public List<Car> findByAvailability(String availability){
        return carRepository.findByAvailability(availability);
    }
}
