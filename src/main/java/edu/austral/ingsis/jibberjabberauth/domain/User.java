package edu.austral.ingsis.jibberjabberauth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.austral.ingsis.jibberjabberauth.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "\"User\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @JsonIgnore
    @NotBlank
    private String password;

    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new Role("USER-ROLE"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isAccountNonExpired();
    }
}
