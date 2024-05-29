package ru.simankovd.videoservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
import java.util.List;

import static ru.simankovd.videoservice.utils.DateUtil.getCurrentDateVideoFormat;

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
                .date(getCurrentDateVideoFormat())
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

        UserDto currentUser = userClient.getCurrentUser(getBearerToken());

        videoDto.setVideoUrl(savedVideo.getVideoUrl());

        // Map the videoDto fields to video
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
//        savedVideo.setVideoStatus(videoDto.getVideoStatus());
        savedVideo.setUserId(currentUser.getUserId());
        savedVideo.setUserNickname(currentUser.getFirstName());

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

        UserDto currentUser = userClient.getCurrentUser(getBearerToken());
        VideoDto videoDto = VideoDto.from(videoById);
        videoDto.setIsSubscribed(currentUser.getSubscribedToUsers().stream().anyMatch(e -> e.equals(videoById.getUserId())));

        if (videoById.getUserId() != null) {
            UserDto author = userClient.getUserById(videoById.getUserId(), getBearerToken());
            videoDto.setSubscribersCount(String.valueOf(author.getSubscribers().size()));

            videoDto.setIsAuthor(videoById.getUserId().equals(currentUser.getUserId()));
        }


        return videoDto;
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

        UserDto currentUser = userClient.getCurrentUser(getBearerToken());

        List<CommentDto> commentsDto = CommentDto.from(videoById.getCommentList());
        // If video owner or comment owner
        commentsDto.forEach(comment -> comment.setIsAuthor(comment.getEmail().equals(currentUser.getEmailAddress()) ||
                videoById.getUserId().equals(currentUser.getUserId())));

        return commentsDto;
    }

    @Override
    public List<VideoDto> getAllVideos() {
        List<Video> videos = videoRepository.findAll();

        return VideoDto.from(videos);
    }

    @Override
    public List<VideoDto> getHistoryAllVideos() {

        UserDto currentUser = userClient.getCurrentUser(getBearerToken());

        List<VideoDto> result = new ArrayList<>();

        currentUser.getVideoHistory()
                .forEach(id -> {
                    if (videoRepository.findById(id).isPresent())
                        result.add(VideoDto.from(videoRepository.findById(id).get()));
                });

        return result;
    }

    @Override
    public List<VideoDto> getSubscriptionsAllVideos() {
        UserDto currentUser = userClient.getCurrentUser(getBearerToken());

        List<VideoDto> result = new ArrayList<>();

        currentUser.getSubscribedToUsers()
                .forEach(id -> {
                    if (videoRepository.findAllByUserId(id) != null)
                        result.addAll(VideoDto.from(videoRepository.findAllByUserId(id)));
                });

        return result;
    }

    @Override
    public List<VideoDto> getLikedAllVideos() {
        UserDto currentUser = userClient.getCurrentUser(getBearerToken());

        List<VideoDto> result = new ArrayList<>();

        currentUser.getLikedVideos()
                .forEach(id -> {
                    if (videoRepository.findById(id).isPresent())
                        result.add(VideoDto.from(videoRepository.findById(id).get()));
                });

        return result;
    }

    @Override
    public List<CommentDto> deleteComment(String videoId, String commentId) {
        Video videoById = getVideoById(videoId);

        UserDto currentUser = userClient.getCurrentUser(getBearerToken());

        videoById.getCommentList().removeIf(comment -> comment.getId().equals(commentId) &&
                (comment.getEmail().equals(currentUser.getEmailAddress()) ||
                videoById.getUserId().equals(currentUser.getUserId())));
        videoRepository.save(videoById);

        return getAllComments(videoId);
    }

    @Override
    public boolean deleteVideo(String videoId) {

        UserDto currentUser = userClient.getCurrentUser(getBearerToken());

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Can not find video by Id - " + videoId));

        boolean isDeleted = false;

        if (video.getUserId().equals(currentUser.getUserId())) {
            String keyVideo = getKey(video.getVideoUrl());
            String keyThumbnail = getKey(video.getThumbnailUrl());
            log.info("Delete video with key = " + keyVideo);
            isDeleted = s3Service.deleteFile(keyVideo);
            log.info("Delete thumbnail with key = " + keyThumbnail);
            s3Service.deleteFile(keyThumbnail);
            videoRepository.delete(video);
        }

        return isDeleted;
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

    private static String getKey(String url) {
        return url.split("/")[url.split("/").length - 1];
    }
}
