package ru.simankovd.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.simankovd.userservice.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String register(Authentication authentication){
        log.info("Start saving information...");
        Jwt jwt = (Jwt) authentication.getPrincipal();

        userService.registerUser(jwt.getTokenValue());

        log.info("End saving information");


        return "User registration succesfully";
    }
}
