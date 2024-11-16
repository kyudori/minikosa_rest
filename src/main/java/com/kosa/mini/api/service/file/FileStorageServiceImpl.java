package com.kosa.mini.api.service.file;

import com.kosa.mini.api.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    private final Path storePhotoLocation;
    private final Path menuPhotoLocation;

    public FileStorageServiceImpl(
            @Value("${file.store-photo-dir}") String storePhotoDir,
            @Value("${file.menu-photo-dir}") String menuPhotoDir
    ) throws Exception {
        this.storePhotoLocation = Paths.get(storePhotoDir).toAbsolutePath().normalize();
        this.menuPhotoLocation = Paths.get(menuPhotoDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.storePhotoLocation);
            Files.createDirectories(this.menuPhotoLocation);
        } catch (Exception ex) {
            throw new Exception("파일 저장 디렉토리를 생성할 수 없습니다.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String directory) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // 파일 이름에 부적절한 문자가 포함되어 있는지 확인
        if(fileName.contains("..")) {
            throw new Exception("파일 이름에 부적절한 문자가 포함되어 있습니다: " + fileName);
        }

        // 고유한 파일 이름 생성
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        Path targetLocation;
        if ("store".equalsIgnoreCase(directory)) {
            targetLocation = this.storePhotoLocation.resolve(uniqueFileName);
        } else if ("menu".equalsIgnoreCase(directory)) {
            targetLocation = this.menuPhotoLocation.resolve(uniqueFileName);
        } else {
            throw new Exception("잘못된 디렉토리 지정: " + directory);
        }

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFileName;
        } catch (IOException ex) {
            throw new FileStorageException("파일을 저장할 수 없습니다: " + fileName, ex);
        }
    }

    public String findByFile(String oldDBPath, MultipartFile photo, String directoryName) throws Exception {
        String targetLocation = "";
        File directory;
        if(directoryName.equals("store")) {
            targetLocation = String.valueOf(this.storePhotoLocation.resolve(oldDBPath));
        }else {
            targetLocation = String.valueOf(this.menuPhotoLocation.resolve(oldDBPath));
        }
        directory = new File(targetLocation);
        String newFileName = "";
        log.info("디렉토리 파일 위치 : {}", directory.toURI());
        if(directory.exists()) {
            directory.delete();
            newFileName = storeFile(photo, directoryName);
            log.info("기존 파일 존재_O, 파일 삭제 진행 : {}",directory.toURI());
            return newFileName;
        }else {
            newFileName = storeFile(photo, directoryName);
            log.info("기존 파일 존재 X ");
            return newFileName;
        }
    }
}
