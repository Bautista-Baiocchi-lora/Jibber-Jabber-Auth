package edu.austral.ingsis.jibberjabberauth.controllers;

import javax.validation.Valid;

import edu.austral.ingsis.jibberjabberauth.domain.dto.CreateUserDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.LoginDto;
import edu.austral.ingsis.jibberjabberauth.domain.dto.UserDto;
import edu.austral.ingsis.jibberjabberauth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public UserDto login(@RequestBody @Valid LoginDto loginDto){
        return userService.login(loginDto);
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
