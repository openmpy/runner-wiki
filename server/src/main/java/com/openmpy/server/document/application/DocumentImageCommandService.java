package com.openmpy.server.document.application;

import com.openmpy.server.image.application.port.ImageStoragePort;
import com.openmpy.server.image.dto.ImagePresignRequest;
import com.openmpy.server.image.dto.ImagePresignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentImageCommandService {

    private final ImageStoragePort imageStoragePort;

    public ImagePresignResponse createPresignedImage(
        final ImagePresignRequest imagePresignRequest
    ) {
        return imageStoragePort.presign(imagePresignRequest);
    }
}
