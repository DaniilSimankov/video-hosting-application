package ru.simankovd.videoservice.service;

import org.springframework.web.multipart.MultipartFile;
import ru.simankovd.videoservice.dto.UploadVideoResponse;
import ru.simankovd.videoservice.dto.VideoDto;

public interface VideoService {

    UploadVideoResponse uploadVideo(MultipartFile multipartFile);

    VideoDto editVideo(VideoDto videoDto);

    String uploadThumbnail(MultipartFile file, String videoId);

    VideoDto getVideoDetails(String videoId);
}
