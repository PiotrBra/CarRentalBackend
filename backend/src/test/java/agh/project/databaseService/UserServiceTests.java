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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationService reservationService;

    @Mock
    private HireService hireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByClientID_UserExists() {
        String clientID = "client123";
        User user = new User(clientID, "Piotr","Branewski" , 123456789, "piotr@dot.net", "pass", "abcabc",  "A", false);
        when(userRepository.findBy_id(clientID)).thenReturn(Optional.of(user));

        User result = userService.findByClientID(clientID);
        assertNotNull(result);
        assertEquals(clientID, result.get_id());
    }

    @Test
    void testFindByClientID_UserNotFound() {
        String clientID = "client123";
        when(userRepository.findBy_id(clientID)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.findByClientID(clientID);
        });
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testDeleteByClientID_UserHasPendingHire() {
        String clientID = "client123";

        // Mocking ReservationService
        Reservation reservation = new Reservation();
        reservation.setReservationDate(new Date(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));
        when(reservationService.findAllByClientID(clientID)).thenReturn(List.of(reservation));

        // Mocking HireService
        Hire hire = new Hire();
        hire.setRentDate(new Date(LocalDateTime.now().minusDays(5), null));
        when(hireService.findAllByClientID(clientID)).thenReturn(List.of(hire));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.deleteByClientID(clientID);
        });
        assertEquals("You can't delete your account. First return the car you borrowed", thrown.getMessage());
    }

    @Test
    void testDeleteByClientID_UserNoPendingHire() {
        String clientID = "client123";

        // Mocking ReservationService
        Reservation reservation = new Reservation();
        reservation.setReservationDate(new Date(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));
        when(reservationService.findAllByClientID(clientID)).thenReturn(List.of(reservation));

        // Mocking HireService
        when(hireService.findAllByClientID(clientID)).thenReturn(List.of());

        userService.deleteByClientID(clientID);

        verify(userRepository, times(1)).deleteBy_id(clientID);
    }

    @Test
    void testUpdateUser_UserExists() {
        String clientID = "client123";
        User existingUser =new User(clientID, "Piotr","Branewski" , 123456789, "piotr@dot.net", "pass", "abcabc",  "A", false);
        User updatedUser = new User(clientID, "Piotr","Branewski" , 987654321, "piotr@dot.net", "pass", "abcabc",  "A", true);

        when(userRepository.findBy_id(clientID)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(updatedUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(clientID, updatedUser);
        assertNotNull(result);
        assertEquals(updatedUser, result);
    }

}
