package agh.project.databaseRepository;

import agh.project.databaseModel.Date;
import agh.project.databaseModel.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class ReservationRepositoryTests {

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    public void setUp() {
        reservationRepository.deleteAll();

        Date date1 = new Date(LocalDateTime.of(2024, 1, 1, 10, 0), LocalDateTime.of(2024, 1, 10, 10, 0));
        Date date2 = new Date(LocalDateTime.of(2024, 2, 1, 10, 0), LocalDateTime.of(2024, 2, 5, 10, 0));

        Reservation reservation1 = new Reservation("1", "client1", "car1", date1, "Confirmed", "No special requests");
        Reservation reservation2 = new Reservation("2", "client1", "car2", date2, "Pending", "Late pickup");
        Reservation reservation3 = new Reservation("3", "client2", "car1", date1, "Cancelled", "Urgent request");

        reservationRepository.saveAll(List.of(reservation1, reservation2, reservation3));
    }

    @Test
    public void testFindById() {
        Optional<Reservation> reservation = reservationRepository.findBy_id("1");
        assertThat(reservation).isPresent();
        assertThat(reservation.get().getCarID()).isEqualTo("car1");
        assertThat(reservation.get().getStatus()).isEqualTo("Confirmed");
    }

    @Test
    public void testFindAllByClientID() {
        List<Reservation> reservations = reservationRepository.findAllByClientID("client1");
        assertThat(reservations).hasSize(2);
        assertThat(reservations.stream().map(Reservation::getCarID).toList()).containsExactlyInAnyOrder("car1", "car2");
    }

    @Test
    public void testFindAllByCarID() {
        List<Reservation> reservations = reservationRepository.findAllByCarID("car1");
        assertThat(reservations).hasSize(2);
        assertThat(reservations.stream().map(Reservation::getClientID).toList()).containsExactlyInAnyOrder("client1", "client2");
    }

    /*@Test
    public void testFindAllByReservationDateBetween() {
        List<Reservation> reservations = reservationRepository.findAllByReservationDateBetween(
                LocalDateTime.of(2024, 1, 1, 0, 0).toString(),
                LocalDateTime.of(2024, 1, 15, 0, 0).toString()
        );
        assertThat(reservations).hasSize(2);
        assertThat(reservations.stream().map(Reservation::getCarID).toList()).containsExactlyInAnyOrder("car1", "car2");
    }

    @Test
    public void testFindByReservationDateBetweenAndCarID() {
        Optional<Reservation> reservation = reservationRepository.findByReservationDateBetweenAndCarID(
                LocalDateTime.of(2024, 1, 1, 0, 0).toString(),
                LocalDateTime.of(2024, 1, 15, 0, 0).toString(),
                "car1"
        );
        assertThat(reservation).isPresent();
        assertThat(reservation.get().getClientID()).isEqualTo("client1");
        assertThat(reservation.get().getStatus()).isEqualTo("Confirmed");
    }*/

    @Test
    public void testDeleteById() {
        reservationRepository.deleteBy_id("1");
        Optional<Reservation> reservation = reservationRepository.findBy_id("1");
        assertThat(reservation).isNotPresent();
    }
}
