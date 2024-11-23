package org.example.testjava;

import org.example.testjava.model.Image;
import org.example.testjava.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication
public class TestJavaApplication implements CommandLineRunner {

	@Autowired
	private ImageService imageService;

	public static void main(String[] args) {
		SpringApplication.run(TestJavaApplication.class, args);
	}

	@Override
	public void run(String... args) {
		String imagePath = "ppsteam.jpg";
		String imagePath2 = "ppsteamPNG.png";
		String currentDir = System.getProperty("user.dir");
		File imageFile = new File(currentDir, imagePath);
		File imageFile2 = new File(currentDir, imagePath2);

		try {
			if (!imageFile.exists()) {
				System.err.println("The image file does not exist at the location: " + imageFile.getAbsolutePath());
				return;
			}

			// Save the image using ImageService
			Image savedImage = imageService.saveImage(imageFile.getAbsolutePath(), LocalDateTime.now());
			imageService.saveImage(imageFile2.getAbsolutePath(), LocalDateTime.now());

			// Retrieve the image using its ID
			Optional<Image> retrievedImage = imageService.getImageById(Long.valueOf(savedImage.getId()));
			if (retrievedImage.isPresent()) {
				Image image = retrievedImage.get();
				System.out.println("Image retrieved successfully!");
				System.out.println("ID: " + image.getId());
				System.out.println("Filepath: " + image.getFilepath());
				System.out.println("File size: " + image.getFileContent() + " bytes");

				// Clean up previous rotated image if it exists
				Path outputPath = Path.of(currentDir, "rotatedImage.jpg");
				if (Files.exists(outputPath)) {
					Files.delete(outputPath);
				}

			} else {
				System.err.println("No image found with ID: " + savedImage.getId());
			}
		} catch (Exception e) {
			System.err.println("Error while processing the image: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
