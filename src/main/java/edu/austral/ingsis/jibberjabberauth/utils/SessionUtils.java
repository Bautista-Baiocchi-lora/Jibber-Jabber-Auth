package edu.austral.ingsis.jibberjabberauth.utils;

import edu.austral.ingsis.jibberjabberauth.domain.JJUser;
import edu.austral.ingsis.jibberjabberauth.exceptions.NotFoundException;
import edu.austral.ingsis.jibberjabberauth.repositories.UserRepository;
import edu.austral.ingsis.jibberjabberauth.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionUtils {

    private final UserRepository userRepository;

    public SessionUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JJUser getUserLogged(){
        Authentication jwt = SecurityContextHolder.getContext().getAuthentication();
        if (jwt == null) throw new NotFoundException("Error while getting session token");

        UserDetailsImpl user = (UserDetailsImpl) jwt.getPrincipal();
        return this.userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new NotFoundException("User does not found"));
    }
}
