package com.openmpy.server.document.application.image.port;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {

    UploadedImage upload(final MultipartFile image);

    void delete(final String url);
}
