package agh.project.databaseRepository;

import agh.project.databaseModel.Opinion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OpinionsRepository extends MongoRepository<Opinion,String> {

    List<Opinion> findAllByCarID(int carID);
    List<Opinion> findAllByClientID(int clientID);

    Optional<Opinion> findByOpinionID(int opinionID);

    void deleteByOpinionID(int opinionID);

}
