package agh.project.databaseModel;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Reservations")
public class Reservation {

    @Id
    private String _id;
    private int clientID;
    private int carID;
    private Date reservationDate;
    private String status;
    private String additionalRequests;

}
