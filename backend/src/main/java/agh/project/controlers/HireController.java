package agh.project.controlers;

import agh.project.databaseModel.Hire;
import agh.project.databaseService.HireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hire")
public class HireController {

    @Autowired
    private HireService hireService;

    //Create
    @PostMapping
    public Hire addHire(@RequestBody Hire hire){
        return hireService.addHire(hire);
    }

    //Read
    @GetMapping
    public List<Hire> getAllHires() {
        return hireService.findAll();
    }

    @GetMapping("carID/{carID}")
    public List<Hire> getAllByCar(@PathVariable String carID){
        return hireService.findAllByCarID(carID);
    }

    @GetMapping("clientID/{clientID}")
    public List<Hire> getAllByClient(@PathVariable String clientID){
        return hireService.findAllByClientID(clientID);
    }

    @GetMapping("/less")
    public List<Hire> getAllCheaperThan(@RequestParam double price){
        return hireService.findAllByPriceLessThan(price);
    }

    @GetMapping("/price-between")
    public List<Hire> findHiresByPriceRange(
            @RequestParam double lowerPrice,
            @RequestParam double upperPrice) {
        return hireService.findAllByPriceBetween(lowerPrice, upperPrice);
    }

    @GetMapping("/time-between")
    public List<Hire> findHiresByTimeRange(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") String start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") String end){
        return hireService.findAllByRentDateBetween(start, end);
    }

    @GetMapping("/status")
    public List<Hire> findHiresByTimeRange(@RequestParam String status) {
        return hireService.findAllByStatus(status);
    }

    //Update
    @PutMapping("/{hireID}")
    public Hire updateHireInfo(@PathVariable String hireID, @RequestBody Hire hire){
        return hireService.updateHireInfo(hireID, hire);
    }

    //Delete
    @DeleteMapping("/{hireID}")
    public void deleteOpinion(@PathVariable String hireID){
        hireService.deleteOpinion(hireID);
    }

}
