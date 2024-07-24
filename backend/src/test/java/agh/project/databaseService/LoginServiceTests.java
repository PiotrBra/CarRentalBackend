package agh.project.databaseService;

import agh.project.databaseModel.User;
import agh.project.databaseRepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticate_Success() {
        User user = new User("1", "John", "Doe", 123456789, "john@example.com", "password", "DL123", "active", false);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        String result = loginService.authenticate("password", "john@example.com");

        assertThat(result).isEqualTo("logged in! as: " + user);
    }

    @Test
    public void testAuthenticate_InvalidPassword() {
        User user = new User("1", "John", "Doe", 123456789, "john@example.com", "password", "DL123", "active", false);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        String result = loginService.authenticate("wrongpassword", "john@example.com");

        assertThat(result).isEqualTo("invalid login or password");
    }

    @Test
    public void testAuthenticate_UserNotFound() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        String result = loginService.authenticate("password", "john@example.com");

        assertThat(result).isEqualTo("invalid login or password");
    }
}
