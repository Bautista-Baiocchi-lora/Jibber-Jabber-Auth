package edu.austral.ingsis.jibberjabberauth.services;

import edu.austral.ingsis.jibberjabberauth.domain.JJUser;
import edu.austral.ingsis.jibberjabberauth.domain.dto.CreateUserDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.JJUserDto;
import edu.austral.ingsis.jibberjabberauth.exceptions.NotFoundException;
import edu.austral.ingsis.jibberjabberauth.factories.UserFactory;
import edu.austral.ingsis.jibberjabberauth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserFactory factory;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserFactory factory) {
        this.repository = userRepository;
        this.factory = factory;
    }

    public JJUserDto getUserById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"))
                .toDto();
    }

    public JJUserDto save(CreateUserDto userDto) {
        final JJUser JJUser = factory.createUser(userDto);
        JJUser.setPassword(bcryptEncoder.encode(JJUser.getPassword()));
        return repository.save(JJUser).toDto();
    }

    public Boolean delete(Long id) {
        repository.deleteById(id);
        return !repository.existsById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final JJUser JJUser = repository.findByUsername(username);

        if(JJUser == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return JJUser;
    }
}
