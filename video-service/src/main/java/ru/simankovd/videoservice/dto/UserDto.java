package ru.simankovd.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String sub;
    private String userId;
    private Set<String> subscribedToUsers = ConcurrentHashMap.newKeySet();
    private Set<String> subscribers = ConcurrentHashMap.newKeySet();
    private Set<String> videoHistory  = ConcurrentHashMap.newKeySet();
    private Set<String> likedVideos  = ConcurrentHashMap.newKeySet();
    private Set<String> dislikedVideos = ConcurrentHashMap.newKeySet();

    public void addToLikeVideos(String videoId){
        likedVideos.add(videoId);
    }

    public void removeFromLikedVideos(String videoId){
        likedVideos.remove(videoId);
    }

    public void addToDislikedVideos(String videoId){
        dislikedVideos.add(videoId);
    }

    public void removeFromDislikedVideos(String videoId){
        dislikedVideos.remove(videoId);
    }

    public void addToVideoHistory(String videoId){
        videoHistory.add(videoId);
    }

    public void addToSubscribedTOUsers(String userId){
        subscribedToUsers.add(userId);
    }

    public void addToSubscribers(String userId){
        subscribers.add(userId);
    }

    public void removeFromSubscribedToUsers(String userId){
        subscribedToUsers.remove(userId);
    }

    public void removeFromSubscribers(String userId){
        subscribers.remove(userId);
    }


}
