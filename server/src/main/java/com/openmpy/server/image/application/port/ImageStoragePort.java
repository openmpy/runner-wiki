package com.openmpy.server.image.application.port;

import com.openmpy.server.image.dto.ImagePresignRequest;
import com.openmpy.server.image.dto.ImagePresignResponse;

public interface ImageStoragePort {

    ImagePresignResponse presign(final ImagePresignRequest request);

    String convertTempToImageUrl(final String content);

    void useImage(final String imageUrl);
}
