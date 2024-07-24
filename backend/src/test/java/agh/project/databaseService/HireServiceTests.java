package agh.project.databaseService;

import agh.project.databaseModel.*;
import agh.project.databaseRepository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HireServiceTests {

    @Mock
    private HireRepository hireRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private HireService hireService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Hire hire = new Hire("1", "client1", new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1)), 100, "car1", new FuelValue(10, 10), "A");
        when(hireRepository.findAll()).thenReturn(List.of(hire));

        List<Hire> hires = hireService.findAll();

        assertThat(hires).containsExactly(hire);
    }

    @Test
    public void testFindAllByClientID() {
        Hire hire = new Hire("1", "client1", new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1)), 100, "car1", new FuelValue(10, 10), "A");
        when(hireRepository.findAllByClientID("client1")).thenReturn(List.of(hire));

        List<Hire> hires = hireService.findAllByClientID("client1");

        assertThat(hires).containsExactly(hire);
    }

    @Test
    public void testAddHire_Success() {
        Hire hire = new Hire("1", "client1", new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1)), 100, "car1", new FuelValue(10, 10), "A");
        when(userRepository.findBy_id("client1")).thenReturn(Optional.of(new User()));
        when(carRepository.findBy_id("car1")).thenReturn(Optional.of(new Car()));
        when(reservationRepository.findAllByCarID("car1")).thenReturn(List.of());
        when(hireRepository.insert(hire)).thenReturn(hire);

        Hire addedHire = hireService.addHire(hire);

        assertThat(addedHire).isEqualTo(hire);
        verify(hireRepository).insert(hire);
    }

    @Test
    public void testAddHire_ClientNotExists() {
        Hire hire = new Hire("1", "client1", new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1)), 100, "car1", new FuelValue(10, 10), "A");
        when(userRepository.findBy_id("client1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hireService.addHire(hire))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not exists");
    }

    @Test
    public void testAddHire_CarOccupied() {
        Hire hire = new Hire("1", "client1", new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1)), 100, "car1", new FuelValue(10, 10), "A");
        when(userRepository.findBy_id("client1")).thenReturn(Optional.of(new User()));
        when(carRepository.findBy_id("car1")).thenReturn(Optional.of(new Car()));

        Reservation conflictingReservation = new Reservation("2", "client2", "car1", new Date(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(2)), "Booked", "");
        when(reservationRepository.findAllByCarID("car1")).thenReturn(List.of(conflictingReservation));

        assertThatThrownBy(() -> hireService.addHire(hire))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Car is occupied");
    }

    @Test
    public void testDeleteOpinion_HireExists() {
        Hire hire = new Hire("1", "client1", new Date(LocalDateTime.now(), LocalDateTime.now().plusDays(1)), 100, "car1", new FuelValue(10, 10), "A");
        when(hireRepository.findBy_id("1")).thenReturn(Optional.of(hire));

        hireService.deleteOpinion("1");

        verify(hireRepository).delete(hire);
    }

    @Test
    public void testDeleteOpinion_HireNotExists() {
        when(hireRepository.findBy_id("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hireService.deleteOpinion("1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("There is no specified hire");
    }
}
