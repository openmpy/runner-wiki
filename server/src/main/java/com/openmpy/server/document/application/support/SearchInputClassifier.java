package com.openmpy.server.document.application.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SearchInputClassifier {

    public boolean isChosungQuery(final String input) {
        if (input == null || input.isBlank()) {
            return false;
        }

        for (final char c : input.toCharArray()) {
            if (c >= 'ㄱ' && c <= 'ㅎ') {
                continue;
            }

            return false;
        }
        return true;
    }

}
