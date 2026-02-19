package com.openmpy.server.image.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openmpy.server.global.properties.S3Properties;
import com.openmpy.server.image.dto.ImagePresignRequest;
import com.openmpy.server.image.dto.ImagePresignResponse;
import java.net.URI;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class S3StoragePortAdapterTest {

    @Autowired
    private S3StoragePortAdapter s3StoragePortAdapter;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @BeforeEach
    void setUp() {
        final S3Properties s3Properties = mock(S3Properties.class);

        s3Client = mock(S3Client.class);
        s3Presigner = mock(S3Presigner.class);
        s3StoragePortAdapter = new S3StoragePortAdapter(s3Properties, s3Client, s3Presigner);

        when(s3Properties.endpoint()).thenReturn("https://test.s3.ap-northeast-2.amazonaws.com");
        when(s3Properties.accessKey()).thenReturn("1234");
        when(s3Properties.secretKey()).thenReturn("1234");
        when(s3Properties.bucket()).thenReturn("test");
        when(s3Properties.region()).thenReturn("ap-northeast-2");
    }

    @DisplayName("Presigned URL을 발행한다.")
    @Test
    void s3_storage_port_adapter_test_01() throws Exception {
        // given
        final ImagePresignRequest request = new ImagePresignRequest("image/png");

        // stub
        final PresignedPutObjectRequest presigned = mock(PresignedPutObjectRequest.class);

        when(presigned.url()).thenReturn(
            URI.create("https://test.s3.ap-northeast-2.amazonaws.com").toURL()
        );
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).thenReturn(
            presigned
        );

        // when
        final ImagePresignResponse response = s3StoragePortAdapter.presign(request);

        // then
        assertThat(response.uploadUrl()).isEqualTo("https://test.s3.ap-northeast-2.amazonaws.com");
        assertThat(response.imageUrl()).startsWith(
                "https://test.s3.ap-northeast-2.amazonaws.com/temp")
            .endsWith(".png");

    }

    @DisplayName("사용된 이미지를 temp 폴더에서 image 폴더로 옮긴다.")
    @Test
    void s3_storage_port_adapter_test_02() {
        // given
        final String url = "https://test.s3.ap-northeast-2.amazonaws.com/temp/image.png";

        // stub
        when(s3Client.copyObject(any(CopyObjectRequest.class))).thenReturn(
            CopyObjectResponse.builder().build()
        );
        when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenReturn(
            DeleteObjectResponse.builder().build()
        );

        // when
        s3StoragePortAdapter.useImage(url);

        // then
        final ArgumentCaptor<Consumer> copyCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(s3Client, times(1)).copyObject(copyCaptor.capture());

        final ArgumentCaptor<Consumer> deleteCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(s3Client, times(1)).deleteObject(deleteCaptor.capture());
    }

    @DisplayName("temp S3 URL을 image URL로 변환한다.")
    @Test
    void s3_storage_port_adapter_test_03() {
        // given
        final String content = """
            내용
            ![image.png](https://test.s3.ap-northeast-2.amazonaws.com/temp/0f8238e3-c77d-49a6-8686-6e41d6b1a1b8.png)
            ![image.png](https://test.s3.ap-northeast-2.amazonaws.com/temp/f15fd5ad-cc57-4474-a7b5-75bcc02e720c.png)
            ![image.png](https://test.s3.ap-northeast-2.amazonaws.com/temp/3a9237aa-6be8-4d09-b18f-aff87672872c.png)
            """;

        // when
        final String result = s3StoragePortAdapter.convertTempToImageUrl(content);

        // then
        assertThat(result).isEqualTo(
            """
                내용
                ![image.png](https://test.s3.ap-northeast-2.amazonaws.com/image/0f8238e3-c77d-49a6-8686-6e41d6b1a1b8.png)
                ![image.png](https://test.s3.ap-northeast-2.amazonaws.com/image/f15fd5ad-cc57-4474-a7b5-75bcc02e720c.png)
                ![image.png](https://test.s3.ap-northeast-2.amazonaws.com/image/3a9237aa-6be8-4d09-b18f-aff87672872c.png)
                """
        );
    }
}