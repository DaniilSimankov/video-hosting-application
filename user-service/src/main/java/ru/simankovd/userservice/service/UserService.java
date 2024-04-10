package ru.simankovd.userservice.service;

import ru.simankovd.userservice.dto.UserDto;
import ru.simankovd.userservice.model.User;

public interface UserService {

    void registerUser(String jwt);

    User getCurrentUser();

    UserDto getCurrentUserDto();

    void addToLikeVideos(String videoId);

    boolean ifLikedVideo(String videoId);

    boolean ifDislikedVideo(String videoId);

    void removeFromLikedVideos(String videoId);

    void removeFromDislikedVideos(String videoId);

}
