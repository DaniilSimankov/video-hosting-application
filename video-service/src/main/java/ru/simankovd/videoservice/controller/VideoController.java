package ru.simankovd.videoservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.simankovd.videoservice.dto.UploadVideoResponse;
import ru.simankovd.videoservice.dto.VideoDto;
import ru.simankovd.videoservice.service.VideoService;

import java.util.List;

@RestController
@RequestMapping("/api/video")
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
        String thumbnailUrl = videoService.uploadThumbnail(file, videoId);
        log.info("End upload thumbnail");

        return thumbnailUrl;
    }

//    @CrossOrigin(origins = "http://localhost:4200")
//    @RequestMapping(value = "/edit", method = RequestMethod.OPTIONS)
//    public ResponseEntity<?> optionsEditVideoMetadata() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Allow", "GET, POST, PUT, DELETE, OPTIONS");
//        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        headers.add("Access-Control-Allow-Headers", "Content-Type, Accept");
//        headers.add("Access-Control-Max-Age", "3600");
//        return ResponseEntity.ok().headers(headers).build();
//    }

//    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<VideoDto> editVideoMetadata(@RequestBody VideoDto videoDto) {
        log.info("Start edit video with Id - {}", videoDto.getId());
        VideoDto dto = videoService.editVideo(videoDto);
        log.info("End edit video with Id - {}", videoDto.getId());

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto getVideoDetails(@PathVariable String videoId) {
        log.info("Start getting video details with Id - {}", videoId);
        VideoDto videoDetails = videoService.getVideoDetails(videoId);
        log.info("End getting video details with Id - {}", videoId);

        return videoDetails;
    }

    @PostMapping("/{videoId}/like")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto likeVideo(@PathVariable String videoId){

        VideoDto response = videoService.likeVideo(videoId);

        return response;

    }
}
