package server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import server.dto.UploadResponse;
import server.service.ImageService;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final ImageService imageService;

    public UploadController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public UploadResponse upload(@RequestParam("file") MultipartFile file) {

        System.out.println("UPLOAD HIT");

        try {
            String url = imageService.saveImage(file);

            System.out.println("IMAGE SAVED: " + url);

            return new UploadResponse("http://192.168.8.44:8080" + url);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}