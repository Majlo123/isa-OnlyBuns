package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


@Service
public class ImageCompressionService {

    private final Path compressedImageDirectoryPath = Paths.get("src", "main", "resources", "static", "compressedImages", "posts");
    private final String compressedImageDirectory = compressedImageDirectoryPath.toAbsolutePath().toString();

    //@Scheduled(cron = "0 0 0 * * ?") // Cron expression for daily execution at midnight
    @Scheduled(cron = "*/1 * * * * ?") // Every second for testing
    public void compressOldImages() {
        String imageDirectory = Paths.get("src", "main", "resources", "static", "images", "posts").toString();

        File dir = new File(imageDirectory);
        File[] files = dir.listFiles();

        System.out.println("Entering conpression");
        if (files != null) {
            System.out.println("Files is not null");
            for (File file : files) {
                if (isOlderThanOneMonth(file) && isUncompressed(file)) {
                    try {

                        System.out.println("Compressing " + file.getName());
                        compressImage(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean isOlderThanOneMonth(File file) {
        //long oneMonthInMillis = 30L * 24 * 60 * 60 * 1000;
        long oneSecondInMillis = (long) 1000; // One second for testing
        return new Date().getTime() - file.lastModified() > oneSecondInMillis;
    }

    private boolean isUncompressed(File file) {
        try{
            File dir = new File(compressedImageDirectory);
            File[] files = dir.listFiles();
            if(files != null) {
                for (File f : files) {
                    String fileName = f.getName();
                    if(fileName.endsWith(file.getName())) return false;
                }
                return true;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void compressImage(File uncompressedFile) throws IOException {

        BufferedImage image = ImageIO.read(uncompressedFile);

        int targetWidth = 500;
        int targetHeight = 500;
        Image scaledImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

        BufferedImage compressedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = compressedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();


        if (!Files.exists(compressedImageDirectoryPath)) {
            Files.createDirectories(compressedImageDirectoryPath);
        }

        Path compressedFilePath = compressedImageDirectoryPath.resolve("compressed_" + uncompressedFile.getName());
        File compressedFile = compressedFilePath.toFile();

        // Write compressed image as JPG
        ImageIO.write(compressedImage, "jpg", compressedFile);
    }
}
