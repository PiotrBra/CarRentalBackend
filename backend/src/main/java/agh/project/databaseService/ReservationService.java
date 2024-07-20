package agh.project.databaseService;

import agh.project.databaseModel.Date;
import agh.project.databaseModel.Reservation;
import agh.project.databaseRepository.CarRepository;
import agh.project.databaseRepository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;


    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findByReservationID(String id){
        Optional<Reservation> optionalReservation = reservationRepository.findBy_id(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        }else {
            throw new RuntimeException("No reservation with specified ID found");
        }
    }

    public List<Reservation> findAllByClientID(String clientID){
        return reservationRepository.findAllByClientID(clientID);
    }
    public List<Reservation> findAllByCarID(String carID){
        return reservationRepository.findAllByCarID(carID);
    }
    public List<Reservation> findAllByReservationDateBetween(String start, String end){
        return reservationRepository.findAllByReservationDateBetween(start,end);
    }
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteByReservationId(String reservationID) {
        reservationRepository.deleteBy_id(reservationID);
    }

    public Reservation cancelReservation(String reservationID){
        Reservation reservation = findByReservationID(reservationID);
        reservation.setStatus("C");
        reservationRepository.save(reservation);
        return reservation;
    }

    public Reservation addReservation(Reservation reservation){
        if (checkAvailabiltiy(reservation.getCarID(),reservation.getReservationDate())){
            reservation.setStatus("A");
            return save(reservation);
        }else{
            throw new RuntimeException("Can't create reservation");
        }

    }

    public Reservation updateReservation( Reservation newReservation){
        Reservation currentReservation = findByReservationID(newReservation.get_id());
        if (checkAvailabiltiy(newReservation.getCarID(), newReservation.getReservationDate())){
            currentReservation.setReservationDate(newReservation.getReservationDate());
            currentReservation.setClientID(newReservation.getClientID());
            currentReservation.setCarID(newReservation.getCarID());
            currentReservation.setAdditionalRequests(newReservation.getAdditionalRequests());
            currentReservation.setStatus("A");
            return save(currentReservation);
        }else{
            throw new RuntimeException("The car in specified dates is already reserved");
        }
    }

    private boolean checkAvailabiltiy(String carID, Date date){
        return reservationRepository.
                findByReservationDateBetweenAndCarID(date.getStart().toString(), date.getEnd().toString(),carID)
                .isEmpty()
                &&
                Period.between(date.getStart().toLocalDate(),date.getEnd().toLocalDate()).toTotalMonths() < 3
                &&
                carRepository.findBy_id(carID).isPresent();
    }
}
