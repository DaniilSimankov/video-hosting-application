package ru.simankovd.videoservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.simankovd.videoservice.dto.User;


@FeignClient("users-service")
public interface UserClient {

    @RequestMapping("/api/user")
    User getCurrentUser();

    @RequestMapping("/api/user/like/{$videoId}")
    void addToLikeVideos(@PathVariable String videoId);

    @RequestMapping("/api/user/video/check/like/{$videoId}")
    boolean ifLikedVideo(String videoId);

    @RequestMapping("/api/user/video/check/dislike/{$videoId}")
    boolean ifDislikedVideo(String videoId);

    @RequestMapping("/api/user/video/remove/like/{$videoId}")
    void removeFromLikedVideos(String videoId);

    @RequestMapping("/api/user/video/remove/dislike/{$videoId}")
    void removeFromDislikedVideos(String videoId);
}
