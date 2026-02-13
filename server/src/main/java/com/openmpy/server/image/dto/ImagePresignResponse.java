package com.openmpy.server.image.dto;

public record ImagePresignResponse(
    String uploadUrl,
    String imageUrl
) {

}
