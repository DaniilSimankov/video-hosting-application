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

    @GetMapping
    public UserDto getCurrentUser(){

        log.info("Get current user");
        return userService.getCurrentUserDto();
    }

    @GetMapping("/like/{videoId}")
    void addToLikeVideos(@PathVariable String videoId){

        log.info("Add to like video with id: " + videoId);
        userService.addToLikeVideos(videoId);
    }

    @GetMapping("/dislike/{videoId}")
    void addToDislikeVideos(@PathVariable String videoId){

        log.info("Add to dislike video with id: " + videoId);
        userService.addToDislikeVideos(videoId);
    }

    @GetMapping("/video/check/like/{videoId}")
    boolean ifLikedVideo(@PathVariable String videoId){

        log.info("Check id liked video with id: " + videoId);
        return userService.ifLikedVideo(videoId);
    }

    @GetMapping("/video/check/dislike/{videoId}")
    boolean ifDislikedVideo(@PathVariable String videoId){

        log.info("Check id disliked video with id: " + videoId);
        return userService.ifDislikedVideo(videoId);
    }

    @GetMapping("/video/remove/like/{videoId}")
    void removeFromLikedVideos(@PathVariable String videoId){

        log.info("Remove from liked video with id: " + videoId);
        userService.removeFromLikedVideos(videoId);

    }

    @GetMapping("/video/remove/dislike/{videoId}")
    void removeFromDislikedVideos(@PathVariable String videoId){

        log.info("Remove from disliked video with id: " + videoId);
        userService.removeFromDislikedVideos(videoId);
    }
}
