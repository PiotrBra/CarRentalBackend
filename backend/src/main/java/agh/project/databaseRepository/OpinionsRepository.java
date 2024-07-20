package agh.project.databaseRepository;

import agh.project.databaseModel.Opinion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OpinionsRepository extends MongoRepository<Opinion,String> {

    List<Opinion> findAllByCarID(String carID);
    List<Opinion> findAllByClientID(String clientID);

    Optional<Opinion> findBy_id(String _id);

    void deleteBy_id(String opinionID);

}
