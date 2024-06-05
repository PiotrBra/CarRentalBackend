package agh.project.databaseService;

import agh.project.databaseModel.Hire;
import agh.project.databaseModel.Reservation;
import agh.project.databaseRepository.CarRepository;
import agh.project.databaseRepository.HireRepository;
import agh.project.databaseRepository.ReservationRepository;
import agh.project.databaseRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HireService {

    @Autowired
    private HireRepository hireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private IdMakerService idMakerService;
    public List<Hire> findAll(){
        return hireRepository.findAll();
    }
    public List<Hire> findAllByClientID(int clientID){
        return hireRepository.findAllByClientID(clientID);
    }
    public List<Hire> findAllByCarID(int carID){
        return hireRepository.findAllByCarID(carID);
    }
    public List<Hire> findAllByPriceLessThan(double price){
        return hireRepository.findAllByPriceLessThan(price);
    }
    public List<Hire> findAllByPriceBetween(double lowerPrice, double upperPrice){
        return hireRepository.findAllByPriceBetween(lowerPrice,upperPrice);
    }
    public List<Hire> findAllByRentDateBetween(String start, String end){
        return hireRepository.findAllByRentDateBetween(start, end);
    }
    public List<Hire> findAllByStatus(String status){
        return hireRepository.findAllByStatus(status);
    }

    public Hire addHire(Hire hire){
        String result = checkAvailabiltiy(hire);
        if (result.isEmpty()){
            hire.setHireID(idMakerService.getAndIncrementHireId());
            hire.setStatus("A");
            return hireRepository.insert(hire);
        }
        throw new RuntimeException("Couldn't insert hire because of: " + result);
    }

    public Hire updateHireInfo(int hireID, Hire updatedHire){
        Optional<Hire> optionalHire = hireRepository.findByHireID(hireID);
        if (optionalHire.isEmpty()){
            throw new RuntimeException("There is no specified hire");
        }
        Hire hire = optionalHire.get();
        String result = checkAvailabiltiy(updatedHire);
        if (result.isEmpty()){
            hire.setStatus(updatedHire.getStatus());
            hire.setFuel(updatedHire.getFuel());
            hire.setPrice(updatedHire.getPrice());
            hire.setCarID(updatedHire.getCarID());
            hire.setClientID(updatedHire.getClientID());
            hire.setRentDate(updatedHire.getRentDate());
            return hireRepository.save(hire);
        }
        throw new RuntimeException("Couldn't insert hire because of: " + result);
    }

    private String checkAvailabiltiy(Hire hire){
        if (userRepository.findByClientID(hire.getClientID()).isEmpty()){
            return "Client not exists";
        }
        if (carRepository.findByCarID(hire.getCarID()).isEmpty()){
            return "Car not exists";
        }
        List<Reservation> reservations = reservationRepository.findAllByCarID(hire.getCarID());
        for (Reservation reservation : reservations){
            //other reservation is in the middle
            if (reservation.getReservationDate().getEnd().isBefore(hire.getRentDate().getEnd()) &&
                    reservation.getReservationDate().getStart().isAfter(hire.getRentDate().getStart())){
                return "Car is occupied";
            }
            //other reservation is happening as the new one would start
            if(reservation.getReservationDate().getStart().isBefore(hire.getRentDate().getStart()) &&
                    reservation.getReservationDate().getEnd().isAfter(hire.getRentDate().getStart())){
                return "Car is occupied";
            }
            //other reservation is happening as the new one would end
            if(reservation.getReservationDate().getStart().isBefore(hire.getRentDate().getEnd()) &&
                    reservation.getReservationDate().getEnd().isAfter(hire.getRentDate().getEnd())){
                return "Car is occupied";
            }
        }
        return "";
    }

    public void deleteOpinion(int hireID) {
        Optional<Hire> optionalHire = hireRepository.findByHireID(hireID);
        if (optionalHire.isEmpty()){
            throw new RuntimeException("There is no specified hire");
        }
        hireRepository.delete(optionalHire.get());
    }
}
