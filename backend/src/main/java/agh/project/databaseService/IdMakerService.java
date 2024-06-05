package agh.project.databaseService;

import agh.project.databaseRepository.*;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import agh.project.databaseModel.*;

import java.util.List;

@Setter
@Service
public class IdMakerService {

    @Autowired
    CarRepository carRepository;
    @Autowired
    HireRepository hireRepository;
    @Autowired
    OpinionsRepository opinionsRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserRepository userRepository;



    private Integer carId = 1;
    private Integer userId = 1;
    private Integer hireId = 1;
    private Integer opinionId = 1;
    private Integer reservationId = 1;

    public synchronized Integer getAndIncrementCarId() {
        this.carId += 1;
        return carId;
    }

    public synchronized Integer getAndIncrementUserId() {
        this.userId += 1;
        return userId;
    }

    public synchronized Integer getAndIncrementHireId() {
        this.hireId += 1;
        return hireId;
    }

    public synchronized Integer getAndIncrementOpinionId() {
        this.opinionId += 1;
        return opinionId;
    }

    public synchronized Integer getAndIncrementReservationId() {
        this.reservationId += 1;
        return reservationId;
    }

    @PostConstruct
    private void initializeIDs(){
        //car
        List<Car> cars = carRepository.findAll();
        int biggestID = 0;
        for (Car car: cars){
            biggestID = Math.max(biggestID, car.getCarID());
        }
        this.carId = biggestID;

        //user
        List<User> users = userRepository.findAll();
        biggestID = 0;
        for (User user: users){
            biggestID = Math.max(biggestID, user.getClientID());
        }
        this.userId = biggestID;

        //hire
        List<Hire> hires = hireRepository.findAll();
        biggestID = 0;
        for (Hire hire: hires){
            biggestID = Math.max(biggestID, hire.getHireID());
        }
        this.hireId = biggestID;

        //opinion
        List<Opinion> opinions = opinionsRepository.findAll();
        biggestID = 0;
        for(Opinion opinion: opinions){
            biggestID = Math.max(biggestID, opinion.getOpinionID());
        }
        this.opinionId = biggestID;

        //reservation
        List<Reservation> reservations = reservationRepository.findAll();
        biggestID = 0;
        for(Reservation reservation : reservations){
            biggestID = Math.max(biggestID, reservation.getReservationID());
        }
        this.reservationId = biggestID;
    }

}
