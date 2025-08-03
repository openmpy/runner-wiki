package com.openmpy.wiki.document.application.request;

public record DocumentCreateRequest(String title, String category, String author, String content) {
}
