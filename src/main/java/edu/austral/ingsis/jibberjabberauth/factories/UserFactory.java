package edu.austral.ingsis.jibberjabberauth.factories;

import edu.austral.ingsis.jibberjabberauth.domain.User;
import edu.austral.ingsis.jibberjabberauth.domain.dto.CreateUserDto;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    public User createUser(CreateUserDto createUserDto){
        return new User(null, createUserDto.getPassword(), createUserDto.getUsername(), createUserDto.getName(), createUserDto.getLastname(), createUserDto.getBirthdate(), createUserDto.getMail());
    }
}
