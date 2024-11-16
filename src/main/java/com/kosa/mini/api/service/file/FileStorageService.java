package com.kosa.mini.api.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveStorePhoto(MultipartFile file) throws Exception;
    String saveMenuPhoto(MultipartFile file) throws Exception;
}
