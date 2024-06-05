package agh.project.databaseRepository;

import agh.project.databaseModel.Hire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HireRepository extends MongoRepository<Hire,String> {

    Optional<Hire> findByHireID(int hireID);
    List<Hire> findAllByClientID(int clientID);
    List<Hire> findAllByCarID(int carID);
    List<Hire> findAllByPriceLessThan(double price);
    List<Hire> findAllByPriceBetween(double lowerPrice, double upperPrice);
    @Query("{ 'reservationDate.start': { $lt: ?1 }, 'reservationDate.end': { $gt: ?0 } }")
    List<Hire> findAllByRentDateBetween(String start, String end);
    List<Hire> findAllByStatus(String status);

}
