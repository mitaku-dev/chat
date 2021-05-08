package de.mfhost.websockets.repository;

import de.mfhost.websockets.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository  extends MongoRepository<User, String> {

    Iterable<User> findByNameStartsWith(String name);
    Optional<User> findByName(String name);
}
