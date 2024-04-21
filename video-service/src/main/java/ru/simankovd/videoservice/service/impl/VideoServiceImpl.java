package ru.simankovd.videoservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.simankovd.videoservice.client.UserClient;
import ru.simankovd.videoservice.dto.CommentDto;
import ru.simankovd.videoservice.dto.UploadVideoResponse;
import ru.simankovd.videoservice.dto.UserDto;
import ru.simankovd.videoservice.dto.VideoDto;
import ru.simankovd.videoservice.model.Comment;
import ru.simankovd.videoservice.model.Video;
import ru.simankovd.videoservice.repository.VideoRepository;
import ru.simankovd.videoservice.service.FileService;
import ru.simankovd.videoservice.service.VideoService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements VideoService {

    private final FileService s3Service;
    private final VideoRepository videoRepository;
    private final UserClient userClient;

    @Override
    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {

        //Upload video file to AWS S3
        String videoUrl = s3Service.uploadFile(multipartFile);
        log.info("AWS S3 video file url: {}", videoUrl);
        var video = Video.builder()
                .videoUrl(videoUrl)
                .build();

        // Save Video Data to Database
        log.info("save video to MongoDB");
        videoRepository.save(video);

        return new UploadVideoResponse(video.getId(), videoUrl);
    }

    @Override
    public VideoDto editVideo(VideoDto videoDto) {
        // Find the video by videoId
        Video savedVideo = getVideoById(videoDto.getId());

        videoDto.setVideoUrl(savedVideo.getVideoUrl());

        // Map the videoDto fields to video
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());
//        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());

        // Save video to the DB
        videoRepository.save(savedVideo);

        return videoDto;
    }

    @Override
    public String uploadThumbnail(MultipartFile file, String videoId) {

        //Upload thumbnail file to AWS S3
        Video savedVideo = getVideoById(videoId);

        String thumbnailUrl = s3Service.uploadFile(file);

        log.info("AWS S3 thumbnail file url: {}", thumbnailUrl);

        savedVideo.setThumbnailUrl(thumbnailUrl);

        // Save Video Data to Database
        log.info("save thumbnail info to MongoDB by videoId = {}", savedVideo.getId());
        videoRepository.save(savedVideo);

        return thumbnailUrl;
    }

    @Override
    public VideoDto getVideoDetails(String videoId) {
        Video videoById = getVideoById(videoId);

        increaseVideoCount(videoById);
        userClient.addVideoToHistory(videoId, getBearerToken());

        return VideoDto.from(videoById);
    }

    /**
     * @param videoId - identification of video
     * @return Video
     */
    Video getVideoById(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find video by Id - " + videoId));
    }


    @Override
    public VideoDto likeVideo(String videoId) {

        // Get video by ID
        Video videoById = getVideoById(videoId);

        // Increment Like Count
        // If user already liked the video,
        // then decrement like video.

        // If user already disliked the video,
        // then increment like count and decrement dislike count

        String jwt = getJwt();

        if (userClient.ifLikedVideo(videoId, getBearerToken())) {
            log.info("Video with id: " + videoId + " was liked");

            videoById.decrementLikes();
            userClient.removeFromLikedVideos(videoId, getBearerToken());
        } else if (userClient.ifDislikedVideo(videoId, getBearerToken())) {
            log.info("Video with id: " + videoId + " was disliked");

            videoById.decrementDislikes();
            userClient.removeFromDislikedVideos(videoId, getBearerToken());

            videoById.incrementLikes();
            userClient.addToLikeVideos(videoId, getBearerToken());
        } else {
            log.info("Video with id: " + videoId + " wasnt liked/disliked");

            videoById.incrementLikes();
            userClient.addToLikeVideos(videoId, getBearerToken());
        }

        videoRepository.save(videoById);

        return VideoDto.from(videoById);
    }

    @Override
    public VideoDto dislikeVideo(String videoId) {

        // Get video by ID
        Video videoById = getVideoById(videoId);

        // Increment Like Count
        // If user already liked the video,
        // then decrement like video.

        // If user already disliked the video,
        // then increment like count and decrement dislike count

        String jwt = getJwt();

        if (userClient.ifLikedVideo(videoId, getBearerToken())) {
            log.info("Video with id: " + videoId + " was liked");

            videoById.decrementLikes();
            userClient.removeFromLikedVideos(videoId, getBearerToken());

            videoById.incrementDislikes();
            userClient.addToDislikeVideos(videoId, getBearerToken());
        } else if (userClient.ifDislikedVideo(videoId, getBearerToken())) {
            log.info("Video with id: " + videoId + " was disliked");

            videoById.decrementDislikes();
            userClient.removeFromDislikedVideos(videoId, getBearerToken());
        } else {
            log.info("Video with id: " + videoId + " wasnt liked/disliked");

            videoById.incrementDislikes();
            userClient.addToDislikeVideos(videoId, getBearerToken());
        }

        videoRepository.save(videoById);

        return VideoDto.from(videoById);
    }

    @Override
    public void addComment(String videoId, CommentDto commentDto) {

        UserDto currentUser = userClient.getCurrentUser(getBearerToken());
        commentDto.setNickname(currentUser.getFirstName());
        commentDto.setEmail(currentUser.getEmailAddress());

        Video videoById = getVideoById(videoId);
        Comment comment = Comment.from(commentDto);
        videoById.addComment(comment);

        videoRepository.save(videoById);
    }

    @Override
    public List<CommentDto> getAllComments(String videoId) {
        Video videoById = getVideoById(videoId);

        return CommentDto.from(videoById.getCommentList());
    }

    @Override
    public List<VideoDto> getAllVideos() {
        List<Video> videos = videoRepository.findAll();

        return VideoDto.from(videos);
    }

    private static String getJwt() {
        return ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getTokenValue();
    }

    private static String getBearerToken() {
        return "Bearer " + getJwt();
    }

    private void increaseVideoCount(Video videoById) {
        videoById.incrementViewCount();
        videoRepository.save(videoById);
    }
}
