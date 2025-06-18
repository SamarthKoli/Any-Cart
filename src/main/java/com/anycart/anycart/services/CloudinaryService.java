package com.anycart.anycart.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        try {
            logger.info("Uploading image to Cloudinary");
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "image"));
            String imageUrl = (String) uploadResult.get("secure_url");
            logger.info("Image uploaded successfully: {}", imageUrl);
            return imageUrl;
        } catch (Exception e) {
            logger.error("Failed to upload image to Cloudinary: {}", e.getMessage());
            throw new IOException("Failed to upload image", e);
        }
    }

    public String getImageUrl(String publicId) {
        try {
            return cloudinary.url().secure(true).generate(publicId);
        } catch (Exception e) {
            logger.error("Failed to generate image URL for publicId {}: {}", publicId, e.getMessage());
            throw new RuntimeException("Failed to generate image URL", e);
        }
    }
}

