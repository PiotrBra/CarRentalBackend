package agh.project.databaseService;

import agh.project.databaseModel.User;
import agh.project.databaseRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    public String authenticate(String password, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)){
            return "logged in! as: " + optionalUser.get();
        }
        return "invalid login or password";
    }
}
