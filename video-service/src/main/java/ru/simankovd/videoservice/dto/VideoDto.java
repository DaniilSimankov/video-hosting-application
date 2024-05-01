package ru.simankovd.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.simankovd.videoservice.enums.VideoStatus;
import ru.simankovd.videoservice.model.Video;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private String authorId;
    private VideoStatus videoStatus;
    private String thumbnailUrl;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
    private Boolean isSubscribed;
    private Boolean isAuthor;
    // todo isSubscribe

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
                .viewCount(video.getViewCount().get())
                .authorId(video.getUserId())
                .build();
    }

    public static List<VideoDto> from(List<Video> videos){
        return videos.stream().map(VideoDto::from).collect(Collectors.toList());
    }
}
