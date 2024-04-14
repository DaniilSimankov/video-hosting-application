package ru.simankovd.videoservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.simankovd.videoservice.dto.CommentDto;
import ru.simankovd.videoservice.dto.UploadVideoResponse;
import ru.simankovd.videoservice.dto.VideoDto;
import ru.simankovd.videoservice.service.VideoService;

import java.util.List;
import java.util.Set;

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
    public VideoDto likeVideo(@PathVariable String videoId) {

        log.info("Start like video with Id - {}", videoId);
        VideoDto response = videoService.likeVideo(videoId);
        log.info("End like video with Id - {}", videoId);

        return response;

    }

    @PostMapping("/{videoId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto dislikeVideo(@PathVariable String videoId) {

        log.info("Start dislike video with Id - {}", videoId);
        VideoDto response = videoService.dislikeVideo(videoId);
        log.info("End dislike video with Id - {}", videoId);

        return response;
    }

    @PostMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void addComment(@PathVariable String videoId, @RequestBody CommentDto commentDto) {

        log.info("Start commenting video with Id - {}", videoId);
        videoService.addComment(videoId, commentDto);
        log.info("End commenting video with Id - {}", videoId);
    }

    @GetMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllComments(@PathVariable String videoId) {

        log.info("Start getting all comments for video with Id - {}", videoId);
        List<CommentDto> comments = videoService.getAllComments(videoId);
        log.info("End getting all comments for video with Id - {}", videoId);

        return comments;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getAllVideos() {

        log.info("Start getting all videos");
        List<VideoDto> videos = videoService.getAllVideos();
        log.info("End getting all videos");

        return videos;
    }

}
