package com.kosa.mini.api.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file, String directory) throws Exception;
    public String findByFile(String oldDBPath, MultipartFile menuPhoto, String directory) throws Exception;
}
