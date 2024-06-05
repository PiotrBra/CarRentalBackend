package agh.project.controlers;

import agh.project.databaseService.UserService;
import agh.project.databaseModel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //Create
    //String firstname, String lastname, int phone, String email, String password, String driverslicence
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    //Read
    @GetMapping
    public List<User> getAllClients() {
        return userService.findAll();
    }

    @GetMapping("/{clientID}")
    public User getClientById(@PathVariable int clientID) {
        return userService.findByClientID(clientID);
    }

    @GetMapping("/firstname/{firstname}")
    public List<User> getUserByFirstname(@PathVariable String firstname){
        return userService.findByFirstname(firstname);
    }

    @GetMapping("/lastname/{lastname}")
    public List<User> getUserByLastName(@PathVariable String lastname){
        return userService.findByLastname(lastname);
    }

    @GetMapping("/phone/{phoneNumber}")
    public User getUserByPhone(@PathVariable int phoneNumber){
        return userService.findByPhone(phoneNumber);
    }

    @GetMapping("/active")
    public List<User> getUsersByStatusActive(){
        return userService.findAllByStatus("A");
    }

    @GetMapping("/occupied")
    public List<User> getUsersByStatusOccupied(){
        return userService.findAllByStatus("O");
    }

    @GetMapping("/blocked")
    public List<User> getUsersByStatusBlocked(){
        return userService.findAllByStatus("B");
    }

    @GetMapping("/employees")
    public List<User> getAllEmployees(){
        return userService.findAllEmployees();
    }

    @GetMapping("/notemployees")
    public List<User> getAllNotEmployees(){
        return userService.findAllNotEmployees();
    }

    //Update
    /*Front info: when doing form to update it make it autofill other
       parameters so client can update just the ones he wants*/
    @PutMapping
    public User updateUser(@RequestParam int clientID, @RequestBody User updatedUser) {
        return userService.updateUser(clientID, updatedUser);
    }

    //Delete
    //delete by ID
    @DeleteMapping
    public void deleteByClientID(@RequestParam int clientID) {
        userService.deleteByClientID(clientID);
    }
}
