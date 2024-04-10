package ru.simankovd.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.simankovd.videoservice.enums.VideoStatus;
import ru.simankovd.videoservice.model.Comment;
import ru.simankovd.videoservice.model.Video;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VideoDto {

    private String id;
    private String title;
    private String description;
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private String thumbnailUrl;
    private Integer likeCount;
    private Integer dislikeCount;

    public static VideoDto from(Video video){
        return VideoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .tags(video.getTags())
                .videoUrl(video.getVideoUrl())
                .videoStatus(video.getVideoStatus())
                .thumbnailUrl(video.getThumbnailUrl())
                .likeCount(video.getLikes().get())
                .dislikeCount(video.getDislikes().get())
                .build();
    }
}
