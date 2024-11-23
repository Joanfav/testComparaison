package org.example.testjava;

import org.example.testjava.model.Image;
import org.example.testjava.service.ImageService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class TestRedimensionnement {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TestJavaApplication.class, args);

        ImageService imageService = context.getBean(ImageService.class);

        String currentDir = System.getProperty("user.dir");
        File outputDir = new File(currentDir);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try {
            Optional<org.example.testjava.model.Image> retrievedImage = imageService.getImageById(1L);
            if (retrievedImage.isEmpty()) {
                System.err.println("No image found with the specified ID!");
                return;
            }

            Image image = retrievedImage.get();
            File imageFile = new File(image.getFilepath());

            if (!imageFile.exists()) {
                System.err.println("The input image file does not exist: " + imageFile.getAbsolutePath());
                return;
            }

            BufferedImage loadedImage = ImageIO.read(imageFile);
            if (loadedImage == null) {
                System.err.println("Failed to load the image, invalid file format or corrupt image.");
                return;
            }

            // Define new dimensions for resizing
            int newWidth = 300;
            int newHeight = 200;
            BufferedImage outputImage = resizeImage(loadedImage, newWidth, newHeight);

            File outputFile = new File(outputDir, "imageResized.jpg");

            boolean isWritten = ImageIO.write(outputImage, "jpg", outputFile);
            if (!isWritten) {
                System.err.println("Failed to write the image to file! Unsupported format or file error.");
            } else {
                System.out.println("Image resized and saved successfully to " + outputFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("An error occurred while resizing the image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }
}