package com.example.server.music.controller;

import com.example.server.music.service.AwsS3Service;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/musicDownload")
@Slf4j
public class MusicDownloadController {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AwsS3Service awsS3Service;
    private static final Logger logger = LoggerFactory.getLogger(MusicController.class);

    public MusicDownloadController(AwsS3Service awsS3Service){
        this.awsS3Service = awsS3Service;
    }


    @GetMapping("/{fileName}")
    public ResponseEntity<String> uploadFile(@PathVariable String fileName) throws IOException {

        return awsS3Service.downLoad(fileName);
        // 파일 이름이 없는 경우
    }



}