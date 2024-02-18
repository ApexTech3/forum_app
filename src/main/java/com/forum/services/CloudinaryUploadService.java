package com.forum.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
@Service
public class CloudinaryUploadService {
    private Cloudinary cloudinary;

    public CloudinaryUploadService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhixuwvrk",
                "api_key", "931492867779326",
                "api_secret", "PtFXxvTkbizlxuhz5n_pteHN3FY"));
    }

    public String uploadImage(File file) {
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (String) uploadResult.get("url");
    }
}
