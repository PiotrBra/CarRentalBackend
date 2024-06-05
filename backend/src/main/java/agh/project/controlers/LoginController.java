package agh.project.controlers;

import agh.project.databaseService.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;

    /* kodowanie hasła w przypadku kontynuacji projektu na frontendzie
const password = encodeURIComponent(<hasło>);
const email = encodeURIComponent(<email>);
const url = `/login?password=${password}&email=${email}`;

fetch(url)
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));
 */

    @GetMapping
    public String authenticate(@RequestParam String password, @RequestParam String email){
        String decodedPassword = URLDecoder.decode(password, StandardCharsets.UTF_8);
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);

        return loginService.authenticate(decodedPassword,decodedEmail);
    }
}
