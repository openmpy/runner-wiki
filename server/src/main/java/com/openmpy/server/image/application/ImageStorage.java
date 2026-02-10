package com.openmpy.server.image.application;

import com.openmpy.server.image.dto.UploadedImage;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {

    UploadedImage upload(final MultipartFile image);

    void delete(final String url);
}
