package agh.project.databaseService;

import agh.project.databaseModel.Hire;
import agh.project.databaseModel.Opinion;
import agh.project.databaseRepository.HireRepository;
import agh.project.databaseRepository.OpinionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpinionService {

    @Autowired
    private OpinionsRepository opinionsRepository;

    @Autowired
    private HireRepository hireRepository;

    @Autowired
    private IdMakerService idMakerService;

    public List<Opinion> findAll(){
        return opinionsRepository.findAll();
    }
    public List<Opinion> findAllByCarID(int carID){
        return opinionsRepository.findAllByCarID(carID);
    }
    public List<Opinion> findAllByClientID(int clientID){
        return opinionsRepository.findAllByClientID(clientID);
    }

    public Opinion save(Opinion opinion){
        return opinionsRepository.save(opinion);
    }

    public Opinion addOpinion(Opinion opinion){
        List<Hire> hires = hireRepository.findAllByClientID(opinion.getClientID());
        for (Hire hire: hires){
            if (hire.getCarID() == opinion.getCarID()){
                opinion.setOpinionID(idMakerService.getAndIncrementOpinionId());
                return save(opinion);
            }
        }
        throw new RuntimeException("The user hasn't hired the car yet!");
    }


    public Opinion updateOpinionDescription(int opinionID, String description) {
        Optional<Opinion> optionalOpinion = opinionsRepository.findByOpinionID(opinionID);
        if (optionalOpinion.isPresent()) {
            Opinion opinion = optionalOpinion.get();
            opinion.setDescription(description);
            return save(opinion);
        }else {
            throw new RuntimeException("No opinion found");
        }
    }

    public void deleteOpinion(int opinionID){
        opinionsRepository.deleteByOpinionID(opinionID);
    }
}
