package org.example.testjava.repository;

import jakarta.transaction.Transactional;
import org.example.testjava.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Transactional
    @Query("SELECT i FROM Image i ORDER BY i.createdAt DESC")
    List<Image> findAllByOrderByCreatedAtDesc();
    @Transactional
    @Query("SELECT i FROM Image i WHERE i.createdAt BETWEEN :start AND :end")
    List<Image> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
