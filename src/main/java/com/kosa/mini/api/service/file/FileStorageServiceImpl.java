package com.kosa.mini.api.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path storePhotoDir;
    private final Path menuPhotoDir;

    public FileStorageServiceImpl(
            @Value("${file.store-photo-dir}") String storePhotoDirPath,
            @Value("${file.menu-photo-dir}") String menuPhotoDirPath) throws IOException {
        this.storePhotoDir = Paths.get(storePhotoDirPath).toAbsolutePath().normalize();
        this.menuPhotoDir = Paths.get(menuPhotoDirPath).toAbsolutePath().normalize();

        // 디렉토리가 존재하지 않으면 생성
        Files.createDirectories(this.storePhotoDir);
        Files.createDirectories(this.menuPhotoDir);
    }

    @Override
    public String saveStorePhoto(MultipartFile file) throws Exception {
        return saveFile(file, storePhotoDir);
    }

    @Override
    public String saveMenuPhoto(MultipartFile file) throws Exception {
        return saveFile(file, menuPhotoDir);
    }

    private String saveFile(MultipartFile file, Path dir) throws Exception {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        // 파일명 검증
        if(originalFilename.contains("..")) {
            throw new IOException("파일명에 부적절한 문자가 포함되어 있습니다: " + originalFilename);
        }

        // 파일명 중복 방지를 위해 UUID 추가
        String fileName = System.currentTimeMillis() + "_" + originalFilename;

        Path targetLocation = dir.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName; // 저장된 파일명을 반환
    }
}
