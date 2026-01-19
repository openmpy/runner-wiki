package com.openmpy.server.dummy;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

class Document {

    private static final int N = 30_000_000;
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void createDocumentCsv() throws Exception {
        final String ts = LocalDateTime.now().format(F);

        try (final BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("document.csv"), StandardCharsets.UTF_8), 1 << 20)
        ) {
            for (int i = 0; i < N; i++) {
                final long id = (long) i + 1;
                final String title = "제목" + i;
                final String category = (i % 2 == 0) ? "USER" : "GUILD";
                final long latestVersion = 1;

                // id,title,category,latest_version,created_at,updated_at
                w.append(Long.toString(id)).append(',')
                        .append(title).append(',')
                        .append(category).append(',')
                        .append(Long.toString(latestVersion)).append(',')
                        .append(ts).append(',')
                        .append(ts)
                        .append('\n');

                if (i % 200_000 == 0 && i != 0) {
                    w.flush();
                }
            }
        }
    }

    @Test
    void createDocumentHistoryCsv() throws Exception {
        final String ts = LocalDateTime.now().format(F);

        try (final BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("document_history.csv"), StandardCharsets.UTF_8), 1 << 20)
        ) {
            for (int i = 0; i < N; i++) {
                final long documentId = (long) i + 1;
                final String author = "작성자";
                final String content = "내용";
                final long version = 1;
                final long size = 8;
                final String ip = "127.0.0.1";

                // document_id,author,content,version,size,client_ip,created_at,updated_at
                w.append(Long.toString(documentId)).append(',')
                        .append(author).append(',')
                        .append(content).append(',')
                        .append(Long.toString(version)).append(',')
                        .append(Long.toString(size)).append(',')
                        .append(ip).append(',')
                        .append(ts).append(',')
                        .append(ts)
                        .append('\n');

                if (i % 200_000 == 0 && i != 0) {
                    w.flush();
                }
            }
        }
    }

    @Test
    void createDocumentHistoriesCsv() throws Exception {
        final String ts = LocalDateTime.now().format(F);

        try (final BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("document_histories.csv"), StandardCharsets.UTF_8), 1 << 20)
        ) {
            for (int i = 0; i < N; i++) {
                final long documentId = 10000000;
                final String author = "작성자" + i;
                final String content = "내용";
                final long version = 2 + i;
                final long size = 8;
                final String ip = "127.0.0.1";

                // document_id,author,content,version,size,client_ip,created_at,updated_at
                w.append(Long.toString(documentId)).append(',')
                        .append(author).append(',')
                        .append(content).append(',')
                        .append(Long.toString(version)).append(',')
                        .append(Long.toString(size)).append(',')
                        .append(ip).append(',')
                        .append(ts).append(',')
                        .append(ts)
                        .append('\n');

                if (i % 200_000 == 0 && i != 0) {
                    w.flush();
                }
            }
        }
    }
}
