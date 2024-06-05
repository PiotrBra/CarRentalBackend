package agh.project.databaseService;

import agh.project.databaseRepository.*;
import agh.project.databaseModel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private HireService hireService;
    @Autowired
    private IdMakerService idMakerService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User client) {
        return userRepository.save(client);
    }

    public User findByClientID(int clientID){
        Optional<User> existingUserOptional = userRepository.findByClientID(clientID);
        if (existingUserOptional.isPresent()){
            return existingUserOptional.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<User> findByFirstname(String firstname){
        return userRepository.findByFirstname(firstname);
    }

    public List<User> findByLastname(String lastname){
        return userRepository.findByLastname(lastname);
    }

    public User findByPhone(int phone){
        Optional<User> existingUserOptional = userRepository.findByPhone(phone);
        if (existingUserOptional.isPresent()){
            return existingUserOptional.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<User> findAllByStatus(String status){
        return userRepository.findAllByStatus(status);
    }
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findAllEmployees() {
        return userRepository.findAllByIsEmployee(true);
    }

    public List<User> findAllNotEmployees(){
        return  userRepository.findAllByIsEmployee(false);
    }

    public User addUser(User user) {
        if (findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("User with specified email already exists");
        }else{
            user.setClientID(idMakerService.getAndIncrementUserId());
            user.setEmployee(false);
            user.setStatus("A");
            return save(user);
        }
    }

    public void deleteByClientID(int clientID) {
        List<Reservation> reservations = reservationService.findAllByClientID(clientID);
        for (Reservation reservation :  reservations){
            if (reservation.getReservationDate().getStart().isAfter(LocalDateTime.now())){
                reservation.setStatus("C");
                reservationService.save(reservation);
            }
        }
        List<Hire> hires = hireService.findAllByClientID(clientID);
        for (Hire hire: hires){
            if (hire.getRentDate().getEnd() == null){
                throw new RuntimeException("You can't delete your account. First return the car you borrowed");
            }
        }
        userRepository.deleteByClientID(clientID);
    }

    public User updateUser(int clientID, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findByClientID(clientID);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            //unique email check
            Optional<User> potentialEmail = userRepository.findByEmail(updatedUser.getEmail());
            if (potentialEmail.isPresent() && !potentialEmail.get().get_id().equals(existingUser.get_id())){
                throw new RuntimeException("specified email already exists");
            }

            existingUser.setFirstname(updatedUser.getFirstname());
            existingUser.setLastname(updatedUser.getLastname());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setDriverslicence(updatedUser.getDriverslicence());
            existingUser.setStatus(updatedUser.getStatus());
            existingUser.setEmployee(updatedUser.isEmployee());
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
