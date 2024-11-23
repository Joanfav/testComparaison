package org.example.testjava;

import org.example.testjava.model.Image;
import org.example.testjava.repository.ImageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class TestTriImage implements CommandLineRunner {

    private final ImageRepository imageRepository;

    public TestTriImage(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TestJavaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String imagePath = "ppsteam.jpg";
        File imageFile = new File(imagePath);

        if (!imageFile.exists()) {
            throw new RuntimeException("Le fichier image n'existe pas à l'emplacement spécifié !");
        }

        byte[] fileContent = Files.readAllBytes(imageFile.toPath());

        Image image1 = new Image(null, imagePath, fileContent, LocalDateTime.of(2020, 1, 1, 12, 0));
        Image image2 = new Image(null, imagePath, fileContent, LocalDateTime.now().minusDays(2));
        Image image3 = new Image(null, imagePath, fileContent, LocalDateTime.now().minusDays(5));

        imageRepository.saveAll(List.of(image1, image2, image3));

        List<Image> allImages = imageRepository.findAllByOrderByCreatedAtDesc();
        System.out.println("All images in the database (sorted by date):");
        allImages.forEach(img -> System.out.println("ID: " + img.getId() + ", Date: " + img.getCreatedAt()));

        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        List<Image> filteredImages = imageRepository.findByCreatedAtBetween(startOfDay, endOfDay);
        System.out.println("\nImages created today:");
        filteredImages.forEach(img -> System.out.println("ID: " + img.getId() + ", Date: " + img.getCreatedAt()));
    }
}
