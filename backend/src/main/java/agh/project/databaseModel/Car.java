package agh.project.databaseModel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Cars")
public class Car {
    @Id
    private String _id;
    private String brand;
    private String model;
    private int year;
    private String registration;
    private int pricePerDay;
    private String category;
    private String availability;
    private String description;
    private Rating ratings;
    private int deposit;
    private String photo;

}
