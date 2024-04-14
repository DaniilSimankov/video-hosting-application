package ru.simankovd.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.simankovd.userservice.model.User;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String firstName;
    private String lastName;
    private String fullName;
    private String emailAddress;
    private Set<String> subscribedToUsers = ConcurrentHashMap.newKeySet();
    private Set<String> subscribers = ConcurrentHashMap.newKeySet();
    private Set<String> videoHistory  = ConcurrentHashMap.newKeySet();
    private Set<String> likedVideos  = ConcurrentHashMap.newKeySet();
    private Set<String> dislikedVideos = ConcurrentHashMap.newKeySet();

    public static UserDto from(User user){
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .emailAddress(user.getEmailAddress())
                .likedVideos(user.getLikedVideos())
                .dislikedVideos(user.getDislikedVideos())
                .videoHistory(user.getVideoHistory())
                .subscribedToUsers(user.getSubscribedToUsers())
                .subscribers(user.getSubscribers())
                .build();
    }

}
