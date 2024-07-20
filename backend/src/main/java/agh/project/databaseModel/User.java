package agh.project.databaseModel;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "Users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String _id;
    private String firstname;
    private String lastname;
    private int phone;
    private String email;
    private String password;
    private String driverslicence;
    private String status;
    private boolean isEmployee;

    public User(String firstname, String lastname, int phone, String email, String password, String driverslicence) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.driverslicence = driverslicence;
    }
}