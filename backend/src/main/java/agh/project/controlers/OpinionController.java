package agh.project.controlers;

import agh.project.databaseModel.Opinion;
import agh.project.databaseService.OpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/opinions")
public class OpinionController {

    @Autowired
    private OpinionService opinionService;

    //Create
    @PostMapping
    public Opinion addOpinion(@RequestBody Opinion opinion){
        return opinionService.addOpinion(opinion);
    }

    //Read
    @GetMapping
    public List<Opinion> getAllOpinions() {
        return opinionService.findAll();
    }

    @GetMapping("carID/{carID}")
    public List<Opinion> getAllByCar(@PathVariable int carID){
        return opinionService.findAllByCarID(carID);
    }

    @GetMapping("clientID/{clientID}")
    public List<Opinion> getAllByClient(@PathVariable int clientID){
        return opinionService.findAllByClientID(clientID);
    }

    //Update
    @PutMapping("/{opinionID}")
    public Opinion updateOpinionDescription(@PathVariable int opinionID, @RequestBody String description){
        return opinionService.updateOpinionDescription(opinionID, description);
    }

    //Delete
    @DeleteMapping("/{opinionID}")
    public void deleteOpinion(@PathVariable int opinionID){
        opinionService.deleteOpinion(opinionID);
    }
}
