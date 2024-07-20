package agh.project.databaseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Opinions")
public class Opinion {
    @Id
    private String _id;
    private int clientID;
    private int carID;
    private String description;
}
