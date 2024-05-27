package ru.simankovd.videoservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file);

    boolean deleteFile(String file);
}
