package ru.simankovd.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.simankovd.userservice.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findBySub(String sub);

    Optional<User> findById(String userId);
}
