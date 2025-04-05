package com.example.dosirakbe.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * packageName    : com.example.dosirakbe.global.config<br>
 * fileName       : CorsMvcConfig<br>
 * author         : femmefatalehaein<br>
 * date           : 10/19/24<br>
 * description    : CORS 설정을 담당하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/19/24        femmefatalehaein                최초 생성<br>
 */
@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    /**
     * 애플리케이션 전역 CORS 정책을 설정합니다.
     *
     * <p>
     * 이 메서드는 {@link WebMvcConfigurer#addCorsMappings(CorsRegistry)}를 오버라이드하여,
     * 모든 경로에 대해 CORS 매핑을 추가합니다. 클라이언트에서 CORS 요청 시 허용되는
     * 헤더 및 동작을 정의합니다.
     * </p>
     *
     * @param corsRegistry CORS 매핑을 설정하기 위한 {@link CorsRegistry} 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedOrigins("*");
    }

    /**
     * CORS 설정을 빈으로 등록합니다.
     *
     * <p>
     * 이 메서드는 {@link CorsConfigurationSource}를 빈으로 등록하며, 특정 도메인, 메서드, 헤더 등을
     * 허용하거나 제한하는 세부적인 CORS 정책을 정의합니다.
     * </p>
     *
     * <ul>
     * <li>허용되는 출처: {@code http://example.com}, {@code http://localhost:8080}</li>
     * <li>허용되는 HTTP 메서드: {@code GET}, {@code POST}, {@code PUT}, {@code DELETE}</li>
     * <li>허용되는 헤더: {@code Authorization}, {@code Content-Type}</li>
     * <li>인증 정보: 허용</li>
     * </ul>
     *
     * @return {@link CorsConfigurationSource} 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://example.com", "http://localhost:8080")); // 허용할 도메인 추가
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}