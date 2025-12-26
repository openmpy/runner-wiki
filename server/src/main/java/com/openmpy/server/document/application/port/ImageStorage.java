package com.openmpy.server.document.application.port;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {

    UploadedImage upload(final MultipartFile image);
}
