package edu.austral.ingsis.jibberjabberauth.factories;

import edu.austral.ingsis.jibberjabberauth.domain.JJUser;
import edu.austral.ingsis.jibberjabberauth.domain.dto.CreateUserDto;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    public JJUser createUser(CreateUserDto createUserDto){
        return new JJUser(null, createUserDto.getPassword(), createUserDto.getUsername(), createUserDto.getName(), createUserDto.getLastname(), createUserDto.getMail());
    }
}
