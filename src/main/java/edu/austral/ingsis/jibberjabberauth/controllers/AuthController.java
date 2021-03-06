package edu.austral.ingsis.jibberjabberauth.controllers;

import javax.validation.Valid;

import edu.austral.ingsis.jibberjabberauth.domain.dto.CreateUserDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.JwtDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.LoginDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.JJUserDto;
import edu.austral.ingsis.jibberjabberauth.security.JwtResponse;
import edu.austral.ingsis.jibberjabberauth.security.JwtTokenUtil;
import edu.austral.ingsis.jibberjabberauth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
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


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginDto loginDto) throws Exception {
        authenticate(loginDto.getMail(), loginDto.getPassword());

       final UserDetails userDetails = this.userService.loadUserByUsername(loginDto.getMail());

       final String token = jwtTokenUtil.generateToken(userDetails);

       return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Boolean> authenticateJwt(){
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{id}")
    public JJUserDto getById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping("/register")
    public JJUserDto register(@RequestBody @Valid CreateUserDto userDto){
        return userService.save(userDto);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable @Valid Long id){
        return userService.delete(id);
    }
}
