package ru.simankovd.userservice.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import ru.simankovd.userservice.dto.UserDto;
import ru.simankovd.userservice.dto.UserInfoDto;
import ru.simankovd.userservice.event.UserRegistrationEvent;
import ru.simankovd.userservice.model.User;
import ru.simankovd.userservice.repository.UserRepository;
import ru.simankovd.userservice.service.UserService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${auth0.userInfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;

    private final KafkaTemplate<String, UserRegistrationEvent> kafkaTemplate;

    @Override
    public String registerUser(String jwt) {
        // Make a call to the userInfo Endpoint
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", jwt))
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        try {
            HttpResponse<String> responseString = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = responseString.body();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserInfoDto userInfoDto = objectMapper.readValue(body, UserInfoDto.class);

            log.info("User nickname: " + userInfoDto.getNickname());

            Optional<User> userBySubject = userRepository.findBySub(userInfoDto.getSub());
            if(userBySubject.isPresent()) {
                return userBySubject.get().getId();
            }

            User user = User.builder()
                    .firstName(userInfoDto.getNickname())
                    .lastName(userInfoDto.getName())
                    .fullName(userInfoDto.getName())
                    .emailAddress(userInfoDto.getEmail())
                    .sub(userInfoDto.getSub())
                    .build();

            // todo add kafka to notification service
            kafkaTemplate.send("registrationTopic", new UserRegistrationEvent(user.getEmailAddress()));

            return userRepository.save(user).getId();
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while registering user", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Fetch user details and save them to the database

    }

    @Override
    public User getCurrentUser() {
        String sub = getSub();

        User user = userRepository.findBySub(sub)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with sub - " + sub));

        return user;
    }

    @Override
    public UserDto getUserDtoById(String userId) {
        return UserDto.from(getUserById(userId));
    }

    @Override
    public UserDto getCurrentUserDto() {
        String sub = getSub();

        User user = userRepository.findBySub(sub)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with sub - " + sub));

        return UserDto.from(user);
    }

    @Override
    public void addToLikeVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToLikeVideos(videoId);
        userRepository.save(currentUser);
    }

    @Override
    public void addToDislikeVideos(String videoId) {

        User currentUser = getCurrentUser();
        currentUser.addToDislikedVideos(videoId);
        userRepository.save(currentUser);
    }

    @Override
    public boolean ifLikedVideo(String videoId) {
        return getCurrentUser().getLikedVideos().stream().anyMatch(likedVideo -> likedVideo.equals(videoId));
    }

    @Override
    public boolean ifDislikedVideo(String videoId) {
        return getCurrentUser().getDislikedVideos().stream().anyMatch(dislikedVideo -> dislikedVideo.equals(videoId));
    }

    @Override
    public void removeFromLikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromLikedVideos(videoId);
        userRepository.save(currentUser);
    }

    @Override
    public void removeFromDislikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromDislikedVideos(videoId);
        userRepository.save(currentUser);
    }

    @Override
    public void addVideoToHistory(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToVideoHistory(videoId);
        userRepository.save(currentUser);
    }

    @Override
    public void subscribeUser(String userId) {

        // Retrieve the current user and add the userId to the subscribed to users set
        User currentUser = getCurrentUser();
        currentUser.addToSubscribedToUsers(userId);

        // Retrieve the target user and add the current user to the subscribers set
        User user = getUserById(userId);
        user.addToSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }

    @Override
    public void unsubscribeUser(String userId) {

        // Retrieve the current user and add the userId to the subscribed to users set
        User currentUser = getCurrentUser();
        currentUser.removeFromSubscribedToUsers(userId);

        // Retrieve the target user and add the current user to the subscribers set
        User user = getUserById(userId);
        user.removeFromSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }

    @Override
    public Set<String> getUserHistory(String userId) {
        User user = getUserById(userId);

        return user.getVideoHistory();
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with userId: " + userId));
    }


    private static String getSub() {
        return ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("sub");
    }
}
