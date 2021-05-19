package edu.austral.ingsis.jibberjabberauth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    private String name;
    private String lastname;
    private String username;
    private String mail;
    private String password;
    private Date birthdate;

}
