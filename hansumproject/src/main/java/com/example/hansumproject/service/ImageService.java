package com.example.hansumproject.service;

import com.example.hansumproject.entity.ImageEntity;
import com.example.hansumproject.repository.ImageRepository;
import com.example.hansumproject.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.Normalizer;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    // 파일 경로 지정
    private  final String FOLDER_PATH = "/Users/kimdonguk/coding/캡스톤3/Hansum-backend/hansumproject/src/main/java/com/example/hansumproject/files/";

    private final ImageRepository imageRepository;

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        log.info("upload file: {}", file.getOriginalFilename());
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        ImageEntity fileData = imageRepository.save(
                ImageEntity.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .filePath(filePath)
                        .build()
        );

        // 파일경로
        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "file uploaded successfully! filePath : " + filePath;
        }

        return null;
    }


    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        log.info("fileName : {}", fileName);
        String newFileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);
        ImageEntity fileData = imageRepository.findByNameContaining(newFileName);
        if (fileData == null){
            log.info("fileData null");
        }
//                .orElseThrow(() -> new RuntimeException("File not found: " + fileName));

        String filePath = fileData.getFilePath();

        log.info("download fileData: {}", fileData);
        log.info("download filePath: {}", filePath);

        return Files.readAllBytes(new File(filePath).toPath());
    }
}

