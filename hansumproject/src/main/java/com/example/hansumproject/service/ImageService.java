package com.example.hansumproject.service;

import com.example.hansumproject.entity.GuesthouseEntity;
import com.example.hansumproject.entity.ImageEntity;
import com.example.hansumproject.repository.GuesthouseRepository;
import com.example.hansumproject.repository.ImageRepository;
import com.example.hansumproject.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    // 파일 경로 지정
    private  final String FOLDER_PATH = "{절대경로}";

    private final ImageRepository imageRepository;

    private final GuesthouseRepository guesthouseRepository;

    // 이미지 파일을 서버 FileSystem에 업로드하고 DB에는 Path를 저장.
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

        // 받은 파일을 filePath에 저장.
        file.transferTo(new File(filePath));

        // 정상적으로 저장되었는지 확인
        if (fileData != null) {
            return "file uploaded successfully! filePath : " + filePath;
        }

        return null;
    }

    // FileSystem에서 이미지를 다운로드하여 byte 배열로 반환
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        log.info("fileName : {}", fileName);
        // GuesthouseEntity에서 url명 가져와서 image 가져와도 될듯.
        GuesthouseEntity guesthouseEntity = guesthouseRepository.findByImageUrlContaining(fileName);
        String test = guesthouseEntity.getImageUrl();
        log.info("test : {}", test);
        String currentWorkingDir = System.getProperty("user.dir");
        String fullPath = Paths.get(currentWorkingDir,"/src/main/java/com/example/hansumproject/files",test).toString();

        log.info("download filePath : {}",fullPath);

        // 파일의 byte 배열을 읽어 반환
        return Files.readAllBytes(new File(fullPath).toPath());
    }
}

