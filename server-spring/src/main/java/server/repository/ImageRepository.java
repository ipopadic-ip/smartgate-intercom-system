package server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import server.model.ImageRecord;

public interface ImageRepository extends JpaRepository<ImageRecord, Long> {
}