package com.example.demo.controller;

import com.example.demo.dto.UploadResponse;
import com.example.demo.service.ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final ImageService imageService;

    public UploadController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public UploadResponse upload(@RequestParam("file") MultipartFile file) {
        String url = imageService.saveImage(file);
        return new UploadResponse("http://192.168.100.109:8080" + url);
    }
}