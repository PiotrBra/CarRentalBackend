package agh.project.databaseService;

import agh.project.databaseModel.Date;
import agh.project.databaseModel.Reservation;
import agh.project.databaseModel.Car;
import agh.project.databaseRepository.CarRepository;
import agh.project.databaseRepository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTests {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Reservation reservation = new Reservation("1", "client1", "car1", new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1)), "A", "No additional requests");
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).containsExactly(reservation);
    }

    @Test
    public void testFindByReservationID() {
        Reservation reservation = new Reservation("1", "client1", "car1", new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1)), "A", "No additional requests");
        when(reservationRepository.findBy_id("1")).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.findByReservationID("1");

        assertThat(foundReservation).isEqualTo(reservation);
    }

    @Test
    public void testFindByReservationID_NotFound() {
        when(reservationRepository.findBy_id("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.findByReservationID("1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No reservation with specified ID found");
    }

    @Test
    public void testAddReservation_Success() {
        Date reservationDate = new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Reservation reservation = new Reservation("1", "client1", "car1", reservationDate, "A", "No additional requests");
        when(carRepository.findBy_id("car1")).thenReturn(Optional.of(new Car()));
        when(reservationRepository.findByReservationDateBetweenAndCarID(
                reservationDate.getStart().toString(),
                reservationDate.getEnd().toString(),
                "car1"))
                .thenReturn(Optional.empty());
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation addedReservation = reservationService.addReservation(reservation);

        assertThat(addedReservation).isEqualTo(reservation);
    }

    @Test
    public void testAddReservation_Failure() {
        Date reservationDate = new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Reservation reservation = new Reservation("1", "client1", "car1", reservationDate, "A", "No additional requests");
        when(carRepository.findBy_id("car1")).thenReturn(Optional.of(new Car()));
        when(reservationRepository.findByReservationDateBetweenAndCarID(
                reservationDate.getStart().toString(),
                reservationDate.getEnd().toString(),
                "car1"))
                .thenReturn(Optional.of(new Reservation()));

        assertThatThrownBy(() -> reservationService.addReservation(reservation))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Can't create reservation");
    }

    @Test
    public void testUpdateReservation_Success() {
        Date reservationDate = new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Reservation existingReservation = new Reservation("1", "client1", "car1", reservationDate, "A", "No additional requests");
        Reservation updatedReservation = new Reservation("1", "client1", "car1", reservationDate, "A", "Updated requests");
        when(reservationRepository.findBy_id("1")).thenReturn(Optional.of(existingReservation));
        when(carRepository.findBy_id("car1")).thenReturn(Optional.of(new Car()));
        when(reservationRepository.findByReservationDateBetweenAndCarID(
                reservationDate.getStart().toString(),
                reservationDate.getEnd().toString(),
                "car1"))
                .thenReturn(Optional.empty());
        when(reservationRepository.save(existingReservation)).thenReturn(updatedReservation);

        Reservation result = reservationService.updateReservation(updatedReservation);

        assertThat(result.getAdditionalRequests()).isEqualTo("Updated requests");
    }

    @Test
    public void testUpdateReservation_Failure() {
        Date reservationDate = new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Reservation newReservation = new Reservation("1", "client1", "car1", reservationDate, "A", "Updated requests");
        when(reservationRepository.findBy_id("1")).thenReturn(Optional.of(new Reservation()));
        when(carRepository.findBy_id("car1")).thenReturn(Optional.of(new Car()));
        when(reservationRepository.findByReservationDateBetweenAndCarID(
                reservationDate.getStart().toString(),
                reservationDate.getEnd().toString(),
                "car1"))
                .thenReturn(Optional.of(new Reservation()));

        assertThatThrownBy(() -> reservationService.updateReservation(newReservation))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("The car in specified dates is already reserved");
    }

    @Test
    public void testCancelReservation() {
        Date reservationDate = new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Reservation reservation = new Reservation("1", "client1", "car1", reservationDate, "A", "No additional requests");
        when(reservationRepository.findBy_id("1")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation canceledReservation = reservationService.cancelReservation("1");

        assertThat(canceledReservation.getStatus()).isEqualTo("C");
        verify(reservationRepository).save(reservation);
    }

    @Test
    public void testDeleteByReservationId() {
        Date reservationDate = new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Reservation reservation = new Reservation("1", "client1", "car1", reservationDate, "A", "No additional requests");
        when(reservationRepository.findBy_id("1")).thenReturn(Optional.of(reservation));

        reservationService.deleteByReservationId("1");

        verify(reservationRepository).deleteBy_id("1");
    }
}
