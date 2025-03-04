package com.friendfinder.friendfindercommon.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@UtilityClass
@Slf4j
public class ImageUtil {

    public static String uploadImage(MultipartFile multipartFile, String path) {
        String fileName;
        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                fileName = System.nanoTime() + "_" + multipartFile.getOriginalFilename();
                File file = new File(path + fileName);
                if (imageFilerChain(file)) {
                    multipartFile.transferTo(file);
                    return fileName;
                }
                throw new IOException("Wrong file format");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static byte[] getBytes(String imageName, String updateImageAndVideo) {
        try {
            File file = new File(updateImageAndVideo + imageName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                return IOUtils.toByteArray(fis);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    private static boolean imageFilerChain(File file) {
        return file.getName().endsWith(".img") || file.getName().endsWith(".jpg")
                || file.getName().endsWith(".png") || file.getName().endsWith(".mp4")
                || file.getName().endsWith(".jpeg") || file.getName().endsWith(".gif")
                || file.getName().endsWith(".svg") || file.getName().endsWith(".webp");
    }
}
