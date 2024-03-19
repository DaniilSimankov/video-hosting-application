package ru.simankovd.videoservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.simankovd.videoservice.dto.UploadVideoResponse;
import ru.simankovd.videoservice.dto.VideoDto;
import ru.simankovd.videoservice.service.VideoService;

@RestController
@RequestMapping("api/video")
@RequiredArgsConstructor
@Slf4j
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file") MultipartFile file) {

        log.info("Start upload video");
        UploadVideoResponse response = videoService.uploadVideo(file);
        log.info("End upload video");

        return response;
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("videoId") String videoId) {

        log.info("Start upload thumbnail");
        String thumbnailUrl = videoService.uploadThumbnail(file,videoId);
        log.info("End upload thumbnail");

        return thumbnailUrl;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDto editVideoMetadata(@RequestBody VideoDto videoDto) {

        log.info("Start edit video with Id - {}", videoDto.getId());
        VideoDto dto = videoService.editVideo(videoDto);
        log.info("End edit video with Id - {}", videoDto.getId());

        return dto;
    }

}
