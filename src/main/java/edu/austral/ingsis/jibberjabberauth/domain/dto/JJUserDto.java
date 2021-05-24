package edu.austral.ingsis.jibberjabberauth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JJUserDto {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private String email;
}
