package com.example.server.music;
import com.example.server.member.config.WebMvcConfig;
import com.example.server.member.security.token.JwtTokenProvider;
import com.example.server.music.controller.MusicController;
import com.example.server.music.service.AwsS3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // 정적 import가 안될 때는 명시적으로 입력 한다.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

//@SpringBootTest
//@AutoConfigureMockMvc // 테스트 애플리케이션의 구성이 자동으로 진행
@WebMvcTest(value = MusicController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import({WebMvcConfig.class, JwtTokenProvider.class}) // 필요한 Configuration 및 빈들을 import
public class MusicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AwsS3Service awsS3Service;

    @Test
    @DisplayName("객체 url 리스트 반환 테스트")
    public void getMusicTest() throws Exception{
        Long themeId = 1L;
        List<String> musicList = Arrays.asList("url1", "url2", "url3", "url4", "url5");

        given(awsS3Service.getMp3FileListUrlV3(themeId)).willReturn(musicList);

        // when - MockMvc 객체로 테스트 대상 Controller 호출

        // then - Controller 핸들러 메서드에서 응답으로 수신한 HTTP Status 및 response body 검증
        mockMvc.perform(
                get("/theme/{theme-id}/music", themeId)) // MockMvcRequestBuilders의  MockHttpServletRequestBuilder에 있는 메소드
                .andExpect(status().isOk()) // http request를 날리면 기본적으로 body 값으로 응답을 받기 때문에 사용함
                .andExpect(jsonPath("$").isArray()) // json 응답이 배열인지 확인
                .andExpect(jsonPath("$.length()").value(musicList.size())
                );

        verify(awsS3Service).getMp3FileListUrlV3(1L);

    }



}