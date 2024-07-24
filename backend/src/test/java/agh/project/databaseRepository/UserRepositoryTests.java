package agh.project.databaseRepository;

import agh.project.databaseModel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        User user1 = new User("1", "John", "Doe", 123456789, "john.doe@example.com", "password123", "DL123", "Active", false);
        User user2 = new User("2", "Jane", "Doe", 987654321, "jane.doe@example.com", "password456", "DL456", "Inactive", true);
        User user3 = new User("3", "Jim", "Beam", 555555555, "jim.beam@example.com", "password789", "DL789", "Active", false);

        userRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    public void testFindById() {
        Optional<User> user = userRepository.findBy_id("1");
        assertThat(user).isPresent();
        assertThat(user.get().getFirstname()).isEqualTo("John");
    }

    @Test
    public void testFindByFirstname() {
        List<User> users = userRepository.findByFirstname("John");
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getLastname()).isEqualTo("Doe");
    }

    @Test
    public void testFindByLastname() {
        List<User> users = userRepository.findByLastname("Doe");
        assertThat(users).hasSize(2);
        assertThat(users.stream().map(User::getFirstname).toList()).containsExactlyInAnyOrder("John", "Jane");
    }

    @Test
    public void testFindByPhone() {
        Optional<User> user = userRepository.findByPhone(123456789);
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("jane.doe@example.com");
        assertThat(user).isPresent();
        assertThat(user.get().getFirstname()).isEqualTo("Jane");
    }

    @Test
    public void testFindAllByStatus() {
        List<User> users = userRepository.findAllByStatus("Active");
        assertThat(users).hasSize(2);
        assertThat(users.stream().map(User::getEmail).toList()).containsExactlyInAnyOrder("john.doe@example.com", "jim.beam@example.com");
    }

    @Test
    public void testFindAllByIsEmployee() {
        List<User> users = userRepository.findAllByIsEmployee(true);
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    public void testDeleteById() {
        userRepository.deleteBy_id("1");
        Optional<User> user = userRepository.findBy_id("1");
        assertThat(user).isNotPresent();
    }

    @Test
    public void testDeleteByEmail() {
        userRepository.deleteByEmail("jim.beam@example.com");
        Optional<User> user = userRepository.findByEmail("jim.beam@example.com");
        assertThat(user).isNotPresent();
    }
}
