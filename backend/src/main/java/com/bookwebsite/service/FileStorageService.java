package com.bookwebsite.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadPath) throws IOException {
        this.uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    public String store(MultipartFile file) throws IOException {
        String cleaned = StringUtils.cleanPath(file.getOriginalFilename());
        Path target = uploadDir.resolve(cleaned);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return cleaned;
    }

    public Path resolve(String filename) {
        return uploadDir.resolve(filename).normalize();
    }
}
