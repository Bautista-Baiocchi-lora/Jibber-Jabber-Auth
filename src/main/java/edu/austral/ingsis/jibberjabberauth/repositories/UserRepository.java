package edu.austral.ingsis.jibberjabberauth.repositories;

import edu.austral.ingsis.jibberjabberauth.domain.JJUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<JJUser, Long> {
    JJUser findByUsername(String username);

    JJUser findByMail(String email);
}
