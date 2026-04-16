package com.example.demo.service;

import com.example.demo.model.ImageRecord;
import com.example.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository repository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ImageService(ImageRepository repository) {
        this.repository = repository;
    }

    public String saveImage(MultipartFile file) {
        try {
            // 1. Koristi apsolutnu putanju
            Path root = Paths.get(uploadDir).toAbsolutePath().normalize();

            // 2. Napravi direktorijum ako ne postoji
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetLocation = root.resolve(fileName);

            // 3. Koristi Files.copy umesto file.transferTo (pouzdanije je sa streamovima)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            ImageRecord record = new ImageRecord();
            record.setFileName(fileName);
            record.setFilePath(targetLocation.toString());
            record.setCreatedAt(LocalDateTime.now());

            repository.save(record);

            return "/images/" + fileName;

        } catch (Exception e) {
            // Ispiši tačnu grešku u konzolu da znaš šta se desilo
            e.printStackTrace();
            throw new RuntimeException("Upload failed: " + e.getMessage(), e);
        }
    }
}
