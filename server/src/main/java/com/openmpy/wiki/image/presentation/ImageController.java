package com.openmpy.wiki.image.presentation;

import com.openmpy.wiki.global.annotaion.PreventDuplicate;
import com.openmpy.wiki.global.utils.ClientIpExtractor;
import com.openmpy.wiki.image.application.ImageService;
import com.openmpy.wiki.image.application.response.ImageUploadResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@RestController
public class ImageController {

    private final ImageService imageService;

    @PreventDuplicate(keyExpression = "'image-upload' + '::' + "
            + "T(com.openmpy.wiki.global.utils.ClientIpExtractor).getClientIp(#servletRequest) + '::' + "
            + "#file.originalFilename + '::' + #file.size"
    )
    @PostMapping
    public ResponseEntity<ImageUploadResponse> uploadImage(
            final MultipartFile file,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        return ResponseEntity.ok(imageService.upload(file, clientIp));
    }
}
