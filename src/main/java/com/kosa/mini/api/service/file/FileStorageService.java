package com.kosa.mini.api.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file, String directory) throws Exception;
}
