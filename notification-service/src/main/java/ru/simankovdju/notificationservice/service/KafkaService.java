package ru.simankovdju.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.simankovdju.notificationservice.event.VideoInfoEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaService {

    private final EmailServiceImpl emailService;
    
    @KafkaListener(topics = "registrationTopic")
    public void handleRegistration(@Payload VideoInfoEvent userRegistrationEvent) {

        log.info("User with email <{}> was registered", userRegistrationEvent.getEmail());

        //send out an email notification
        // todo add logic
        emailService.sendMessageWithAttachment(
                "thedanils63@mail.ru",//userRegistrationEvent.getEmail(),
                "Регистрация прошла успешно!",
                "Поздравляю! Ваша почта " + userRegistrationEvent.getEmail() + " была успешно зарегестрирована в нашей системе!"
        );
    }

    @KafkaListener(topics = "editVideoTopic")
    public void handleUploadVideo(VideoInfoEvent videoInfoEvent) {

        log.info("Video by {} with id <{}> was uploaded", videoInfoEvent.getEmail(), videoInfoEvent.getVideoId());
        log.info("Please, go to <http://localhost:4200/save-video-details/{}> to edit metadata", videoInfoEvent.getVideoId());

        //send out an email notification
        // todo add logic
        emailService.sendMessageWithAttachment(
                "thedanils63@mail.ru", //userRegistrationEvent.getEmail(),
                "Загрузка видео прошла успешно!",
                "Дорогой, " + videoInfoEvent.getEmail() + "!\nПоздравляю! Выше видео было успешно загружено. Пожалуйста, пройдите по ссылке http://localhost:4200/save-video-details/" + videoInfoEvent.getVideoId() +
                        ", чтобы добавить информацию о видео."
        );
    }

    @KafkaListener(topics = "viewVideoTopic")
    public void handleEditVideo(VideoInfoEvent videoInfoEvent) {

        log.info("Video by {} with title <{}> was uploaded", videoInfoEvent.getEmail(), videoInfoEvent.getVideoTitle());
        log.info("Please, go to <http://localhost:4200/video-details/{}> to watch Video", videoInfoEvent.getVideoId());


        //send out an email notification
        // todo add logic
        emailService.sendMessageWithAttachment(
                "thedanils63@mail.ru", //userRegistrationEvent.getEmail(),
                "Обновление данных видео прошло успешно!",
                "Дорогой, " + videoInfoEvent.getEmail() + "!\nПоздравляю! Выше видео " + videoInfoEvent.getVideoTitle() + " было успешно обновлено. " +
                        "Пожалуйста, пройдите по ссылке http://localhost:4200/video-details/" + videoInfoEvent.getVideoId() +
                        ", чтобы добавить информацию о видео."
        );
    }

    @KafkaListener(topics = "deleteVideoTopic")
    public void handleDeleteVideo(VideoInfoEvent videoInfoEvent) {

        log.info("Video by {} with title <{}> and id <{}> was deleted", videoInfoEvent.getEmail(), videoInfoEvent.getVideoTitle(), videoInfoEvent.getVideoId());

        //send out an email notification
        // todo add logic
        emailService.sendMessageWithAttachment(
                "thedanils63@mail.ru", //userRegistrationEvent.getEmail(),
                "Удаление видео прошло успешно!",
                "Дорогой, " + videoInfoEvent.getEmail() + "!\nПоздравляю! Выше видео " + videoInfoEvent.getVideoTitle() + " было успешно удалено. "
        );
    }
}
