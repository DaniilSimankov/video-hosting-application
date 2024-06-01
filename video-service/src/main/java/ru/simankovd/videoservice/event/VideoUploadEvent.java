package ru.simankovd.videoservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadEvent {
    private String email;
    private String videoTitle;
    private String videoId;
}
