package agh.project.databaseRepository;

import agh.project.databaseModel.Hire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HireRepository extends MongoRepository<Hire,String> {

    Optional<Hire> findBy_id(String _id);
    List<Hire> findAllByClientID(String clientID);
    List<Hire> findAllByCarID(String carID);

    @Query("{'price': {$lte:  ?0}}")
    List<Hire> findAllByPriceLessThan(double price);

    @Query("{ 'price': { $lte: ?1, $gte: ?0 }}")
    List<Hire> findAllByPriceBetween(double lowerPrice, double upperPrice);

    List<Hire> findAllByStatus(String status);

}
