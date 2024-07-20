package agh.project.databaseModel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Hires")
public class Hire {
    @Id
    private String _id;
    private int clientID;
    private Date rentDate;
    private int price;
    private int carID;
    private FuelValue fuel;
    private String status;
}