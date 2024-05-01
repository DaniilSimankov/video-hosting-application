package ru.simankovd.videoservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.simankovd.videoservice.dto.UserDto;


@FeignClient(value = "user-service")
public interface UserClient {

    String AUTH_TOKEN = "Authorization";

    @RequestMapping("/api/user")
    UserDto getCurrentUser(@RequestHeader(AUTH_TOKEN) String bearerToken);

    @RequestMapping("/api/user/like/{videoId}")
    void addToLikeVideos(@PathVariable("videoId") String videoId, @RequestHeader(AUTH_TOKEN) String bearerToken);

    @RequestMapping("/api/user/dislike/{videoId}")
    void addToDislikeVideos(@PathVariable("videoId") String videoId, @RequestHeader(AUTH_TOKEN) String bearerToken);

    @RequestMapping("/api/user/video/check/like/{videoId}")
    boolean ifLikedVideo(@PathVariable("videoId") String videoId, @RequestHeader(AUTH_TOKEN) String bearerToken);

    @RequestMapping("/api/user/video/check/dislike/{videoId}")
    boolean ifDislikedVideo(@PathVariable("videoId") String videoId, @RequestHeader(AUTH_TOKEN) String bearerToken);

    @RequestMapping("/api/user/video/remove/like/{videoId}")
    void removeFromLikedVideos(@PathVariable("videoId") String videoId, @RequestHeader(AUTH_TOKEN) String bearerToken);

    @RequestMapping("/api/user/video/remove/dislike/{videoId}")
    void removeFromDislikedVideos(@PathVariable("videoId") String videoId, @RequestHeader(AUTH_TOKEN) String bearerToken);

    @RequestMapping("/api/user/video/{videoId}")
    void addVideoToHistory(@PathVariable("videoId") String videoId, @RequestHeader(AUTH_TOKEN) String bearerToken);

    @RequestMapping("/api/user/{userId}")
    UserDto getUserById(@PathVariable("userId") String userId, @RequestHeader(AUTH_TOKEN) String bearerToken);
}
