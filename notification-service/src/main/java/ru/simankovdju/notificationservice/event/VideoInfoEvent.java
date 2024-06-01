package ru.simankovdju.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfoEvent {
    private String email;
    private String videoTitle;
    private String videoId;
}
