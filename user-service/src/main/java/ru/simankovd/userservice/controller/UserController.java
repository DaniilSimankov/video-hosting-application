package ru.simankovd.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.simankovd.userservice.dto.UserDto;
import ru.simankovd.userservice.dto.UserInfoDto;
import ru.simankovd.userservice.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String register(Authentication authentication) {
        log.info("Start saving information...");
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String userId = userService.registerUser(jwt.getTokenValue());

        log.info("End saving information");

        return userId;
    }

    @PostMapping("/subscribe/{userId}")
    public boolean subscribeUser(@PathVariable String userId) {

        log.info("Start subscribing to user with id: " + userId);
        userService.subscribeUser(userId);
        log.info("End subscribing");

        return true;
    }

    @PostMapping("/unsubscribe/{userId}")
    public boolean unsubscribeUser(@PathVariable String userId) {

        log.info("Start unsubscribing to user with id: " + userId);
        userService.unsubscribeUser(userId);
        log.info("End unsubscribing");

        return true;
    }

    @GetMapping("/{userId}/history")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> userHistory(@PathVariable String userId) {

        log.info("Start getting history");
        Set<String> history = userService.getUserHistory(userId);
        log.info("End getting history");

        return history;
    }

    @GetMapping
    public UserDto getCurrentUser() {

        log.info("Get current user");
        return userService.getCurrentUserDto();
    }

    ///////////////////////////////////////////

    @GetMapping("/like/{videoId}")
    void addToLikeVideos(@PathVariable String videoId) {

        log.info("Add to like video with id: " + videoId);
        userService.addToLikeVideos(videoId);
    }

    @GetMapping("/dislike/{videoId}")
    void addToDislikeVideos(@PathVariable String videoId) {

        log.info("Add to dislike video with id: " + videoId);
        userService.addToDislikeVideos(videoId);
    }

    @GetMapping("/video/check/like/{videoId}")
    boolean ifLikedVideo(@PathVariable String videoId) {

        log.info("Check if liked video with id: " + videoId);
        return userService.ifLikedVideo(videoId);
    }

    @GetMapping("/video/check/dislike/{videoId}")
    boolean ifDislikedVideo(@PathVariable String videoId) {

        log.info("Check if disliked video with id: " + videoId);
        return userService.ifDislikedVideo(videoId);
    }

    @GetMapping("/video/remove/like/{videoId}")
    void removeFromLikedVideos(@PathVariable String videoId) {

        log.info("Remove from liked video with id: " + videoId);
        userService.removeFromLikedVideos(videoId);

    }

    @GetMapping("/video/remove/dislike/{videoId}")
    void removeFromDislikedVideos(@PathVariable String videoId) {

        log.info("Remove from disliked video with id: " + videoId);
        userService.removeFromDislikedVideos(videoId);
    }


    @GetMapping("/video/{videoId}")
    void addVideoToHistory(@PathVariable String videoId) {

        log.info("Start adding to history video with id: " + videoId);
        userService.addVideoToHistory(videoId);
        log.info("End adding to history video with id: " + videoId);

    }


}
