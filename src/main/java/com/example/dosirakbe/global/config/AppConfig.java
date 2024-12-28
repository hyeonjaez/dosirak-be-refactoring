package com.example.dosirakbe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * packageName    : com.example.dosirakbe.global.config<br>
 * fileName       : AppConfig<br>
 * author         : Yujin_Lee<br>
 * date           : 10/27/24<br>
 * description    : 애플리케이션의 전역 설정을 담당하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/27/24        Yujin Lee                최초 생성<br>
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate 빈을 생성합니다.
     *
     * <p>
     * 이 메서드는 {@link RestTemplate}을 애플리케이션 컨텍스트에 빈(Bean)으로 등록합니다.
     * {@link RestTemplate}은 RESTFul 웹 서비스와의 상호작용을 간편하게 처리할 수 있는 스프링 제공 HTTP 클라이언트입니다.
     * </p>
     *
     * @return {@link RestTemplate} 객체
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
