package com.example.server.music.service;

import com.example.server.music.service.AwsS3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.Mockito.*;

class AwsS3ServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    @InjectMocks
    private AwsS3Service awsS3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 아래와 같이 주입하면서 mocking할 수도 있습니다.
        // awsS3Service.setS3Client(s3Client);
        // awsS3Service.setS3Presigner(s3Presigner);
    }

    @Test
    @DisplayName("음원 조회 테스트")
    void testGetMp3FileListUrlV3() {
        // 테스트용 데이터
        long themeId = 123L;

        // CompletableFuture가 완료될 때까지 대기하기 위한 시간 (초)
        long timeoutSeconds = 2;

        // 테스트용 데이터를 mocking된 객체의 응답으로 설정
        // (실제로 S3 서비스를 호출하지 않고 가상의 응답을 반환하도록 설정)
        when(s3Client.listObjects(any(ListObjectsRequest.class)))
                .thenReturn(ListObjectsResponse.builder().contents(Collections.emptyList()).build());
        when(s3Client.headObject(any(HeadObjectRequest.class)))
                .thenReturn(HeadObjectResponse.builder().metadata(Collections.singletonMap("themeid", String.valueOf(themeId))).build());


        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenAnswer(invocation -> {
                    CompletableFuture<PresignedGetObjectRequest> presignedFuture = new CompletableFuture<>();

                    // GetObjectPresignRequest를 통해 필요한 정보를 추출하거나 처리
                    GetObjectPresignRequest getObjectPresignRequest = invocation.getArgument(0);

                    // 예상 결과를 생성
                    PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(r ->
                                    r.getObjectRequest(getObjectPresignRequest.getObjectRequest())
                                            .signatureDuration(getObjectPresignRequest.signatureDuration())
                    );

                    // CompletableFuture를 완료시킴
                    presignedFuture.complete(presignedGetObjectRequest);

                    return presignedFuture;
                });

        // 실제로 메서드를 호출하면서 CompletableFuture의 결과를 기다림
        assertTimeoutPreemptively(Duration.ofSeconds(timeoutSeconds), () -> {
            List<String> result = awsS3Service.getMp3FileListUrlV3(themeId);
            assertEquals(Collections.singletonList("http://example.com"), result);
        });

        // 메서드가 내부에서 호출한 S3Client 및 S3Presigner 메서드가 예상대로 호출되었는지 확인
        verify(s3Client, atLeastOnce()).listObjects(any(ListObjectsRequest.class));
        verify(s3Client, atLeastOnce()).headObject(any(HeadObjectRequest.class));
        verify(s3Presigner, atLeastOnce()).presignGetObject(any(GetObjectPresignRequest.class));
    }
}
