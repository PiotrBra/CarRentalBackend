package agh.project.databaseModel;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rating {
    private int bad;
    private int good;
    private int excellent;
}
