package ru.simankovd.videoservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.simankovd.videoservice.model.Video;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {

    Optional<Video> findById(String id);
    List<Video> findAllByUserId(String userId);
}
