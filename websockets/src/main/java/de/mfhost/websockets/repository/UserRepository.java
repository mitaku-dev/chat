package de.mfhost.websockets.repository;

import de.mfhost.websockets.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository  extends MongoRepository<User, String> {

    Iterable<User> findByNameStartsWith(String name);
}
