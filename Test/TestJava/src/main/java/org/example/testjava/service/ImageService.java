package org.example.testjava.service;

import org.example.testjava.model.Image;
import org.example.testjava.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;


    @Transactional
    public Image saveImage(String path, LocalDateTime now) throws IOException {
        if (!Files.exists(Path.of(path))) {
            throw new IllegalArgumentException("Le fichier image n'existe pas !");
        }

        byte[] fileContent = Files.readAllBytes(Path.of(path));

        Image newImage = new Image();
        newImage.setFilepath(path);
        newImage.setFileContent(fileContent);
        newImage.setCreatedAt(now);

        Image savedImage = imageRepository.save(newImage);

        System.out.println("Image insérée avec succès !");
        return savedImage;
    }

    @Transactional(readOnly = true)
    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

}
