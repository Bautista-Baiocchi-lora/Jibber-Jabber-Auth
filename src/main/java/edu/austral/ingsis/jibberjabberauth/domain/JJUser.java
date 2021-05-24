package edu.austral.ingsis.jibberjabberauth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.austral.ingsis.jibberjabberauth.domain.dto.JJUserDto;
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
@Table(name = "jj_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JJUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(updatable = true, insertable = true)
    private String password;

    @Column(updatable = true, insertable = true, unique = true)
    private String username;

    @Column(updatable = false)
    private String name;

    @Column(updatable = false)
    private String lastname;

    @Column(updatable = true, insertable = true, unique = true)
    private String mail;

    public JJUserDto toDto(){
        return new JJUserDto(
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
        return true;
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
