package edu.austral.ingsis.jibberjabberauth.controllers;

import javax.validation.Valid;

import edu.austral.ingsis.jibberjabberauth.domain.dto.CreateUserDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.LoginDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.UserDto;
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


    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> createJwtToken(@RequestBody @Valid LoginDto loginDto) throws Exception {
        authenticate(loginDto.getUsername(), loginDto.getPassword());

       final UserDetails userDetails = this.userService.loadUserByUsername(loginDto.getUsername());

       final String token = jwtTokenUtil.generateToken(userDetails);

       return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable String id){
        return userService.getUserById(id);
    }

    @PostMapping("/save")
    public UserDto save(@RequestBody @Valid CreateUserDto userDto){
        return userService.save(userDto);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable @Valid String id){
        return userService.delete(id);
    }
}
