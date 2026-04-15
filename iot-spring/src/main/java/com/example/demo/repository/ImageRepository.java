package com.example.demo.repository;

import com.example.demo.model.ImageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageRecord, Long> {
}