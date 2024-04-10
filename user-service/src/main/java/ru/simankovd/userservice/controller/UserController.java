package ru.simankovd.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.simankovd.userservice.dto.UserDto;
import ru.simankovd.userservice.dto.UserInfoDto;
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

    @GetMapping("/api/user")
    public UserDto getCurrentUser(){

        return userService.getCurrentUserDto();
    }

    @GetMapping("/api/user/like/{$videoId}")
    void addToLikeVideos(@PathVariable String videoId){

        userService.addToLikeVideos(videoId);
    }

    @GetMapping("/api/user/video/check/like/{$videoId}")
    boolean ifLikedVideo(String videoId){

        return userService.ifLikedVideo(videoId);
    }

    @GetMapping("/api/user/video/check/dislike/{$videoId}")
    boolean ifDislikedVideo(String videoId){

        return userService.ifDislikedVideo(videoId);
    }

    @GetMapping("/api/user/video/remove/like/{$videoId}")
    void removeFromLikedVideos(String videoId){

        userService.removeFromLikedVideos(videoId);

    }

    @GetMapping("/api/user/video/remove/dislike/{$videoId}")
    void removeFromDislikedVideos(String videoId){

        userService.removeFromDislikedVideos(videoId);
    }
}
