package agh.project.controlers;

import agh.project.databaseModel.Reservation;
import agh.project.databaseService.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    //Create
    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.addReservation(reservation);
    }

    //Read
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/{reservationID}")
    public Reservation getReservationById(@PathVariable String reservationID) {
        return reservationService.findByReservationID(reservationID);
    }
    @GetMapping("/clientID/{clientID}")
    public List<Reservation> getAllClientsReservation(@PathVariable String clientID){
        return reservationService.findAllByClientID(clientID);
    }
    @GetMapping("/carID/{carID}")
    public List<Reservation> getAllCarsReservation(@PathVariable String carID){
        return reservationService.findAllByCarID(carID);
    }

    @GetMapping("/between")
    public List<Reservation> getReservationsBetweenDates(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") String start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") String end) {
        return reservationService.findAllByReservationDateBetween(start, end);
    }


    //Update
    @PutMapping()
    public Reservation updateReservation(@RequestBody Reservation reservation) {
        return reservationService.updateReservation(reservation);
    }
    @PutMapping("/cancel/{reservationID}")
    public Reservation cancelReservation(@PathVariable String reservationID){
        return reservationService.cancelReservation(reservationID);
    }

    @DeleteMapping
    public void deleteReservation(@RequestParam String reservationID) {
        reservationService.deleteByReservationId(reservationID);
    }

}