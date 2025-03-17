package com.nottie.service;

import com.cloudinary.Cloudinary;
import com.nottie.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file, String folderName) {
        try {
            HashMap<String, String> options = new HashMap<>();
            options.put("folder", folderName);
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = uploadedFile.get("public_id").toString();
            return cloudinary.url().secure(true).generate(publicId);
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
