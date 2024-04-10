package ru.simankovd.videoservice.service;

import org.springframework.web.multipart.MultipartFile;
import ru.simankovd.videoservice.dto.UploadVideoResponse;
import ru.simankovd.videoservice.dto.VideoDto;

public interface VideoService {

    UploadVideoResponse uploadVideo(MultipartFile multipartFile);

    VideoDto editVideo(VideoDto videoDto);

    String uploadThumbnail(MultipartFile file, String videoId);

    VideoDto getVideoDetails(String videoId);

    /**
     * Increment Like Count
     * If user already liked the video,
     * then decrement like video.
     * If user already disliked the video,
     * then increment like count and decrement dislike count
     * @param videoId id
     * @return
     */
    VideoDto likeVideo(String videoId);
}
