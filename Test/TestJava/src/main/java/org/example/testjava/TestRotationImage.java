package org.example.testjava;

import org.example.testjava.model.Image;
import org.example.testjava.service.ImageService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class TestRotationImage {
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

            BufferedImage outputImage = rotateImageByDegrees(loadedImage, 90);

            File outputFile = new File(outputDir, "imageRotated.jpg");

            boolean isWritten = ImageIO.write(outputImage, "jpg", outputFile);
            if (!isWritten) {
                System.err.println("Failed to write the image to file! Unsupported format or file error.");
            } else {
                System.out.println("Image rotated and saved successfully to " + outputFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("An error occurred while rotating the image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
}
