package edu.austral.ingsis.jibberjabberauth.domain;

import edu.austral.ingsis.jibberjabberauth.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String password;
    private String username;
    private String name;
    private String lastname;
    private String mail;

    public UserDto toDto(){
        return new UserDto(
                id,
                username,
                name,
                lastname,
                mail
        );
    }
}
