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
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserFactory factory;

    @Autowired
    public UserService(UserRepository userRepository, UserFactory factory) {
        this.repository = userRepository;
        this.factory = factory;
    }

    public UserDto login(LoginDto loginDto) {
        User user = repository.findById(loginDto.getUsername()).orElseThrow(() -> new NotFoundException("User does not found"));
        if (user.getPassword().equals(loginDto.getPassword())){
            return user.toDto();
        }
        throw new InvalidRequest("Wrong email or password");
    }

    public UserDto getUserById(String id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"))
                .toDto();
    }


    public UserDto save(CreateUserDto userDto) {
        return repository.save(factory.createUser(userDto)).toDto();
    }

    public Boolean delete(String id) {
        repository.deleteById(id);
        return !repository.existsById(id);
    }
}
