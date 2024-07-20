package agh.project.databaseRepository;

import agh.project.databaseModel.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findBy_id(String clientID);
    List<User> findByFirstname(String firstname);
    List<User> findByLastname(String lastname);
    Optional<User> findByPhone(int phone);
    Optional<User> findByEmail(String email);

    List<User> findAllByStatus(String status);

    List<User> findAllByIsEmployee(boolean isEmployee);

    void deleteBy_id(String clientID);

    void deleteByEmail(String email);

    //żeby coś dodać robisz
    //User findBy(lub findAllBy)<pole z dużej litery>() tak jak u góry
}