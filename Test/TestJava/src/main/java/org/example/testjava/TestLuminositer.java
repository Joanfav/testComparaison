package org.example.testjava;

import org.example.testjava.model.Image;
import org.example.testjava.service.ImageService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class TestLuminositer {
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

            double brightnessFactor = 0.5;
            BufferedImage outputImage = adjustBrightness(loadedImage, brightnessFactor);

            File outputFile = new File(outputDir, "imageBrightnessAdjusted.jpg");

            boolean isWritten = ImageIO.write(outputImage, "jpg", outputFile);
            if (!isWritten) {
                System.err.println("Failed to write the image to file! Unsupported format or file error.");
            } else {
                System.out.println("Image brightness adjusted and saved successfully to " + outputFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("An error occurred while adjusting the brightness of the image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static BufferedImage adjustBrightness(BufferedImage img, double factor) {
        BufferedImage adjustedImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgb = img.getRGB(x, y);

                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                red = Math.min((int) (red * factor), 255);
                green = Math.min((int) (green * factor), 255);
                blue = Math.min((int) (blue * factor), 255);

                int newRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                adjustedImage.setRGB(x, y, newRGB);
            }
        }
        return adjustedImage;
    }
}
