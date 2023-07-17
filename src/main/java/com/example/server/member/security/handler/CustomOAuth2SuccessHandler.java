package com.example.server.member.security.handler;

import com.example.server.member.dto.MemberIdAndTokenDto;
import com.example.server.member.dto.ResponseDto;
import com.example.server.member.entity.Member;
import com.example.server.member.entity.RefreshToken;
import com.example.server.member.repository.MemberJpaRepository;
import com.example.server.member.security.token.JwtTokenProvider;
import com.example.server.member.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Value("${jwt.refresh_token_expired}")
    long refreshTokenExpired;
    private final MemberJpaRepository memberJpaRepository;
    private final JwtTokenProvider tokenProvider;
    private final TokenService tokenService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String username = oAuth2User.getAttribute("name");


        Member member = memberJpaRepository.findByMemberUsername(username).get();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());

        String refreshToken = tokenService.createRefreshToken(username);
        String accessToken = tokenProvider.createToken(authenticationToken);

        redisTemplate.opsForValue().set("RT:" + member.getId(), refreshToken, refreshTokenExpired, TimeUnit.MILLISECONDS);

        ResponseDto responseDto = ResponseDto.builder()
                .memberId(member.getId())
                .refreshToken(refreshToken)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseDto);

        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}