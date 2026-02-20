package com.openmpy.server.image.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.properties.S3Properties;
import com.openmpy.server.image.dto.ImagePresignRequest;
import com.openmpy.server.image.dto.ImagePresignResponse;
import java.net.URI;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
            ㅋㅋㅋㅋㅋㅋ
            
            ![4a1fd56f-8cd4-4058-876b-2db466ea99ca.png](https://test.s3.ap-northeast-2.amazonaws.com/temp/5e6acc88-90ac-4953-b3a8-8ee7c6fb7453.png)![75ce8262-c04d-46fb-9e6a-d238490e357b.png](https://test.s3.ap-northeast-2.amazonaws.com/temp/47e11dd5-ae32-4066-94dd-cdd1d3f32b1a.png)
            """;

        // when
        final String result = s3StoragePortAdapter.convertTempToImageUrl(content);

        // then
        assertThat(result).isEqualTo(
            """
                ㅋㅋㅋㅋㅋㅋ
                
                ![4a1fd56f-8cd4-4058-876b-2db466ea99ca.png](https://test.s3.ap-northeast-2.amazonaws.com/image/5e6acc88-90ac-4953-b3a8-8ee7c6fb7453.png)![75ce8262-c04d-46fb-9e6a-d238490e357b.png](https://test.s3.ap-northeast-2.amazonaws.com/image/47e11dd5-ae32-4066-94dd-cdd1d3f32b1a.png)
                """
        );
    }

    @DisplayName("내용이 비어있으면 빈 값 그대로 반환한다.")
    @ParameterizedTest(name = "입력: {0}")
    @NullAndEmptySource
    void s3_storage_port_adapter_test_04(final String input) {
        // when
        final String result = s3StoragePortAdapter.convertTempToImageUrl(input);

        // then
        assertThat(result).isNullOrEmpty();
    }

    @DisplayName("지원하지 않는 컨텐츠 타입일 경우 예외가 발생한다.")
    @Test
    void exception_s3_storage_port_adapter_test_01() {
        // given
        final ImagePresignRequest request = new ImagePresignRequest("image/test");

        // when & then
        assertThatThrownBy(() -> s3StoragePortAdapter.presign(request))
            .isInstanceOf(CustomException.class)
            .hasMessage("지원하지 않는 Content Type 입니다.");
    }

    @DisplayName("URL이 잘못된 형식이면 예외가 발생한다.")
    @Test
    void exception_s3_storage_port_adapter_test_02() {
        // given
        final String url = "invalid-url";

        // when & then
        assertThatThrownBy(() -> s3StoragePortAdapter.useImage(url))
            .isInstanceOf(CustomException.class)
            .hasMessage("잘못된 URL 형식입니다.");
    }

    @DisplayName("URL에서 이미지 타입을 찾을 수 없으면 예외가 발생한다.")
    @Test
    void exception_s3_storage_port_adapter_test_03() {
        // given
        final String url = "https://test.s3.ap-northeast-2.amazonaws.com/temp";

        // when & then
        assertThatThrownBy(() -> s3StoragePortAdapter.useImage(url))
            .isInstanceOf(CustomException.class)
            .hasMessage("잘못된 키 값입니다.");
    }
}