package ru.simankovd.userservice.service;

import ru.simankovd.userservice.dto.UserDto;
import ru.simankovd.userservice.model.User;

import java.util.Set;

public interface UserService {

    String registerUser(String jwt);

    User getCurrentUser();

    UserDto getCurrentUserDto();

    void addToLikeVideos(String videoId);

    void addToDislikeVideos(String videoId);

    boolean ifLikedVideo(String videoId);

    boolean ifDislikedVideo(String videoId);

    void removeFromLikedVideos(String videoId);

    void removeFromDislikedVideos(String videoId);

    void addVideoToHistory(String videoId);

    void subscribeUser(String userId);

    void unsubscribeUser(String userId);

    Set<String> getUserHistory(String userId);

    UserDto getUserDtoById(String userId);
}
