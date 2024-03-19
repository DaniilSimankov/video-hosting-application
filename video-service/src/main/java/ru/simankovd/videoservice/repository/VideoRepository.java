package ru.simankovd.videoservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.simankovd.videoservice.model.Video;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
}
