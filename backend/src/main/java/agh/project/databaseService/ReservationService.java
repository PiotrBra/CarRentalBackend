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

    @Autowired
    private IdMakerService idMakerService;

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findByReservationID(int reservationID){
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationID(reservationID);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        }else {
            throw new RuntimeException("No reservation with specified ID found");
        }
    }

    public List<Reservation> findAllByClientID(int clientID){
        return reservationRepository.findAllByClientID(clientID);
    }
    public List<Reservation> findAllByCarID(int carID){
        return reservationRepository.findAllByCarID(carID);
    }
    public List<Reservation> findAllByReservationDateBetween(String start, String end){
        return reservationRepository.findAllByReservationDateBetween(start,end);
    }
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteByReservationId(int reservationID) {
        reservationRepository.deleteByReservationID(reservationID);
    }

    public Reservation cancelReservation(int reservationID){
        Reservation reservation = findByReservationID(reservationID);
        reservation.setStatus("C");
        reservationRepository.save(reservation);
        return reservation;
    }

    public Reservation addReservation(Reservation reservation){
        if (checkAvailabiltiy(reservation.getCarID(),reservation.getReservationDate())){
            reservation.setReservationID(idMakerService.getAndIncrementReservationId());
            reservation.setStatus("A");
            return save(reservation);
        }else{
            throw new RuntimeException("Can't create reservation");
        }

    }

    public Reservation updateReservation( Reservation newReservation){
        Reservation currentReservation = findByReservationID(newReservation.getReservationID());
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

    private boolean checkAvailabiltiy(int carID, Date date){
        return reservationRepository.
                findByReservationDateBetweenAndCarID(date.getStart().toString(), date.getEnd().toString(),carID)
                .isEmpty()
                &&
                Period.between(date.getStart().toLocalDate(),date.getEnd().toLocalDate()).toTotalMonths() < 3
                &&
                carRepository.findByCarID(carID).isPresent();
    }
}
