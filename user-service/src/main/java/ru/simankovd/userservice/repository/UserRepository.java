package ru.simankovd.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.simankovd.userservice.model.User;

public interface UserRepository extends MongoRepository<User, String> {
}
