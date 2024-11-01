package com.example.dosirakbe.domain.auth.service;

import com.example.dosirakbe.domain.auth.dto.response.AppleTokenResponse;
import org.apache.commons.io.IOUtils;
import com.example.dosirakbe.domain.auth.dto.*;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final RestTemplate restTemplate;

    @Value("${apple.path}")
    private String path;

    @Value("${apple.url}")
    private String appleUrl;

    @Value("${apple.cid}")
    private String clientId;

    @Value("${apple.tid}")
    private String teamId;

    @Value("${apple.kid}")
    private String keyId;

    public KakaoUserInfo processKakaoToken(String kakaoAccessToken) {
        return getUserInfoFromKakao(kakaoAccessToken);
    }

    private KakaoUserInfo getUserInfoFromKakao(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return new KakaoUserInfo(response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ApiException(ExceptionEnum.INVALID_ACCESS_TOKEN);
            }
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }

    public NaverUserInfo processNaverToken(String naverAccessToken) {
        return getUserInfoFromNaver(naverAccessToken);
    }

    private NaverUserInfo getUserInfoFromNaver(String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return new NaverUserInfo(response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ApiException(ExceptionEnum.INVALID_ACCESS_TOKEN);
            }
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }

    public AppleUserInfo processAppleToken(String idToken) {
        Map<String, Object> jwtClaims = decodeJwtTokenPayload(idToken);
        return new AppleUserInfo(jwtClaims);
    }

    private AppleTokenResponse getIdTokenFromApple(String authorizationCode) {
        String url = "https://appleid.apple.com/auth/token";
        String clientSecret;
        try {
            clientSecret = createClientSecret();
        } catch (Exception e) {
            throw new RuntimeException("Apple client secret 생성 오류", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", authorizationCode);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                return new AppleTokenResponse(
                        (String) responseBody.get("access_token"),
                        (String) responseBody.get("refresh_token"),
                        (String) responseBody.get("id_token"),
                        (Long) responseBody.get("expires_in")
                );
            } else {
                throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
        }
    }

    public void processAppleWithdraw(String refreshToken) {
        String url = "https://appleid.apple.com/auth/revoke";
        String clientSecret;

        try {
            clientSecret = createClientSecret();
        } catch (Exception e) {
            throw new RuntimeException("Apple client secret 생성 오류", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("token", refreshToken);
        params.add("token_type_hint", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Apple 연결 해제 요청 실패", e);
        }
    }


    public PrivateKey getPrivateKey() throws IOException {

        ClassPathResource resource = new ClassPathResource(path);

        InputStream in = resource.getInputStream();
        PEMParser pemParser = new PEMParser(new StringReader(IOUtils.toString(in, StandardCharsets.UTF_8)));
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        return converter.getPrivateKey(object);
    }


    public String createClientSecret() throws IOException {
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", keyId);
        jwtHeader.put("alg", "ES256");

        return Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(teamId)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 - UNIX 시간
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))// 만료 시간
                .setAudience(appleUrl)
                .setSubject(clientId)
                .signWith(getPrivateKey(), io.jsonwebtoken.SignatureAlgorithm.ES256)
                .compact();
    }


    public Map<String, Object> decodeJwtTokenPayload(String jwtToken) {
        Map<String, Object> jwtClaims = new HashMap<>();
        try {
            String[] parts = jwtToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(decodedString, Map.class);
            jwtClaims.putAll(map);

        } catch (JsonProcessingException e) {
        }
        return jwtClaims;
    }





}