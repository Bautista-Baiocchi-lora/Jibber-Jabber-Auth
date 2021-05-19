package edu.austral.ingsis.jibberjabberauth.repositories;

import edu.austral.ingsis.jibberjabberauth.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);
}
