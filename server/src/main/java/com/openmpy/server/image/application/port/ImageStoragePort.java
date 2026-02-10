package com.openmpy.server.image.application.port;

import com.openmpy.server.image.dto.UploadedImage;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStoragePort {

    UploadedImage upload(final MultipartFile image);

    void delete(final String url);
}
