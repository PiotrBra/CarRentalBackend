package agh.project.controlers;


import agh.project.databaseModel.Car;
import agh.project.databaseService.CarService;
import agh.project.databaseService.IdMakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    @Autowired
    private CarService carService;


    @GetMapping("/")
    public List<Car> getAllCars(){
        return carService.findAll();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable int id){
        return carService.findByID(id);
    }
    @GetMapping("/lessthan/{price}")
    public List<Car> getCarByPriceLessThan(@PathVariable double price){
        return carService.findByPriceLessThan(price);
    }

    @GetMapping("/greaterthan/{price}")
    public List<Car> getCarByPriceGreaterThan(@PathVariable double price){
        return carService.findByPriceGreaterThan(price);
    }

    @GetMapping("/between/{lowerPrice}/{upperPrice}")
    public List<Car> getCarByPriceBetween(@PathVariable double lowerPrice,
                                @PathVariable double upperPrice){
        return carService.findByPriceBetween(lowerPrice, upperPrice);
    }

    @GetMapping("/availability/{ava}")
    public List<Car> getCarByAvailability(@PathVariable String ava){
        return carService.findByAvailability(ava);
    }


}
