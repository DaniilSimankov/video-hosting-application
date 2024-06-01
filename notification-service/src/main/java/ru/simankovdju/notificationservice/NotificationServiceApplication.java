package ru.simankovdju.notificationservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import ru.simankovdju.notificationservice.event.UserRegistrationEvent;
import ru.simankovdju.notificationservice.event.VideoInfoEvent;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "registrationTopic")
    public void handleRegistration(UserRegistrationEvent userRegistrationEvent) {

        log.info("User with email <{}> was registered", userRegistrationEvent.getEmail());

        //send out an email notification
        // todo add logic
    }

    @KafkaListener(topics = "editVideoTopic")
    public void handleUploadVideo(VideoInfoEvent videoInfoEvent) {

        log.info("Video by {} with id <{}> was uploaded", videoInfoEvent.getEmail(), videoInfoEvent.getVideoId());
        log.info("Please, go to <http://localhost:4200/save-video-details/{}> to edit metadata", videoInfoEvent.getVideoId());

        //send out an email notification
        // todo add logic
    }

    @KafkaListener(topics = "viewVideoTopic")
    public void handleEditVideo(VideoInfoEvent videoInfoEvent) {

        log.info("Video by {} with title <{}> was uploaded", videoInfoEvent.getEmail(), videoInfoEvent.getVideoTitle());
        log.info("Please, go to <http://localhost:4200/video-details/{}> to watch Video", videoInfoEvent.getVideoId());


        //send out an email notification
        // todo add logic
    }

    @KafkaListener(topics = "deleteVideoTopic")
    public void handleDeleteVideo(VideoInfoEvent videoInfoEvent) {

        log.info("Video by {} with title <{}> and id <{}> was deleted", videoInfoEvent.getEmail(), videoInfoEvent.getVideoTitle(), videoInfoEvent.getVideoId());

        //send out an email notification
        // todo add logic
    }
}

