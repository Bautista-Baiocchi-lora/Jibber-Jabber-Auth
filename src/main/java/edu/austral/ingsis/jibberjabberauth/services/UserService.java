package edu.austral.ingsis.jibberjabberauth.services;

import edu.austral.ingsis.jibberjabberauth.domain.User;
import edu.austral.ingsis.jibberjabberauth.domain.dto.CreateUserDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.LoginDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.UserDto;
import edu.austral.ingsis.jibberjabberauth.exceptions.InvalidRequest;
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

    public UserDto getUserById(String id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"))
                .toDto();
    }

    public UserDto save(CreateUserDto userDto) {
        final User user = factory.createUser(userDto);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return repository.save(user).toDto();
    }

    public Boolean delete(String id) {
        repository.deleteById(id);
        return !repository.existsById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = repository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }
}
