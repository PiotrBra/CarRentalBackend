package agh.project.databaseRepository;

import agh.project.databaseModel.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    Optional<Reservation> findBy_id(String _id);
    List<Reservation> findAllByClientID(String clientID);
    List<Reservation> findAllByCarID(String carID);
    @Query("{ 'reservationDate.start': { $lt: ?1 }, 'reservationDate.end': { $gt: ?0 } }")
    List<Reservation> findAllByReservationDateBetween(String startDate, String endDate);
    @Query("{ 'reservationDate.start': { $lt: ?1 }, 'reservationDate.end': { $gt: ?0 }, 'carID':  {$eq:  ?2}}")
    Optional<Reservation> findByReservationDateBetweenAndCarID(String startDate, String endDate, String carID);

    void deleteBy_id(String _id);
    //add more if necessary
}
