package edu.austral.ingsis.jibberjabberauth.services;

import edu.austral.ingsis.jibberjabberauth.domain.JJUser;
import edu.austral.ingsis.jibberjabberauth.domain.dto.ChangePasswordDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.CreateUserDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.JJUserDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.LoginDto;
import edu.austral.ingsis.jibberjabberauth.exceptions.NotFoundException;
import edu.austral.ingsis.jibberjabberauth.factories.UserFactory;
import edu.austral.ingsis.jibberjabberauth.repositories.UserRepository;
import edu.austral.ingsis.jibberjabberauth.security.JwtTokenUtil;
import edu.austral.ingsis.jibberjabberauth.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserFactory factory;
    private final SessionUtils sessionUtils;
    private final PasswordEncoder bcryptEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;


    @Autowired
    public UserService(UserRepository userRepository, UserFactory factory, SessionUtils sessionUtils, PasswordEncoder bcryptEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.repository = userRepository;
        this.factory = factory;
        this.sessionUtils = sessionUtils;
        this.bcryptEncoder = bcryptEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
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

    public Boolean changePassword(ChangePasswordDto changePasswordDto) {
        JJUser user = sessionUtils.getUserLogged();
        try {
            authenticate(user.getUsername(), changePasswordDto.getOldPassword());
            user.setPassword(bcryptEncoder.encode(changePasswordDto.getNewPassword()));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    private JJUser getUserByUsername(String username){
        return repository.findByUsername(username).orElseThrow(() -> new NotFoundException("User does not found"));
    }

    public Boolean login(LoginDto loginDto, HttpServletResponse response) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateJwtToken(authentication);
        //add Secure for HTTPS !!
        response.addHeader("Set-Cookie", "jwt=" + token + "; HttpOnly; SameSite=strict; Path=/;");
        return !token.isEmpty();
    }
}
