package agh.project.databaseRepository;



import agh.project.databaseModel.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends MongoRepository<Car,String> {
    Optional<Car> findByCarID(int carID);
    List<Car> findByBrand(String brand);

    @Query("{ 'pricePerDay' : { $lt: ?0 } }")
    List<Car> findByPriceLessThan(double price);
    @Query("{ 'pricePerDay' : { $gt: ?0 } }")
    List<Car> findByPriceGreaterThan(double price);

    @Query("{ 'pricePerDay' : { $gte: ?0, $lte: ?1 } }")
    List<Car> findByPriceBetween(double lowerPrice, double higherPrice);

    @Query("{'availability' :  ?0}")
    List<Car> findByAvailability(String availability);


}
