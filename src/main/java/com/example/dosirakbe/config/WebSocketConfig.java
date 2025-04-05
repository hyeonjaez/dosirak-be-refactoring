package com.example.dosirakbe.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.global.config<br>
 * fileName       : WebSocketConfig<br>
 * author         : Fiat_lux<br>
 * date           : 11/09/24<br>
 * description    : WebSocket 및 STOMP 프로토콜을 사용한 메시징 설정을 담당하는 구성 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/09/24        Fiat_lux                최초 생성<br>
 */
@Configuration
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커를 활성화,  STOMP 프로토콜을 사용한 WebSocket 메시징 기능
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * STOMP 엔드포인트를 등록하고, SockJS 폴백을 활성화합니다.
     *
     * <p>
     * 이 메서드는 클라이언트가 WebSocket 연결을 시작할 수 있는 엔드포인트를 등록합니다.
     * 지정된 엔드포인트에 대해 모든 출처(origin)를 허용하며, SockJS를 통해 WebSocket 을 지원하지 않는 브라우저에서도 대체 통신이 가능하도록 설정합니다.
     * </p>
     *
     * @param registry STOMP 엔드포인트를 등록할 {@link StompEndpointRegistry} 객체
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/dosirak")
                .setAllowedOriginPatterns("*");// 테스트를 위해 모든 출처 허용
//                .addInterceptors(new JwtHandshakeInterceptor(jwtUtil));
//                .withSockJS(); // 엔드포인트 지정 및 withSockJS() SockJS 폴백 활성화
        // 브라우저가 WebSocket을 지원하지 않는 경우에도 HTTP 기반의 대체 통신이 가능

    }

    /**
     * 메시지 브로커를 구성하고, 애플리케이션의 메시지 경로 접두사를 설정합니다.
     *
     * <p>
     * 이 메서드는 애플리케이션의 메시지 경로 접두사를 `/app`으로 설정하고, 간단한 메시지 브로커를 활성화하여
     * `/topic` 및 `/queue` 경로로 전송된 메시지를 브로드캐스트 및 개인 메시지 큐로 전달합니다.
     * </p>
     *
     * @param config 메시지 브로커 구성을 위한 {@link MessageBrokerRegistry} 객체
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        // 메시지 전송 경로 접두사 예를들면, /app/message 로 메세지 요청시
        // 서버의 특정 메서드 인 @MessageMapping 이 메세지를 처리한
        config.enableSimpleBroker("/topic", "/queue");
        // 간단한 메시지 브로커 활성화
        //topic : 여러 클라이언트가 구독하는 브로드캐스트 채널
        // queue : 1:1 메시지를 주고받는 개인 메세지 큐 경로
    }

    /**
     * Jackson 메시지 컨버터를 빈으로 등록하고, Java 8 날짜/시간 모듈을 설정합니다.
     *
     * <p>
     * 이 메서드는 {@link ObjectMapper}를 커스터마이징하여 Java 8 날짜/시간 타입을 지원하고,
     * 날짜 및 시간 값을 ISO 형식으로 직렬화하도록 설정합니다.
     * </p>
     *
     * @return 설정된 {@link MappingJackson2MessageConverter} 객체
     */
    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Java 8 날짜/시간 지원 모듈 등록
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // 시간 값을 ISO 형식으로 직렬화
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper); // ObjectMapper 설정
        return converter;
    }

    /**
     * 메시지 컨버터를 구성하여 문자열 및 JSON 메시지를 처리할 수 있도록 설정합니다.
     *
     * <p>
     * 이 메서드는 {@link StringMessageConverter}와 커스터마이징된 {@link MappingJackson2MessageConverter}를
     * 메시지 컨버터 리스트에 추가하여, 다양한 메시지 형식을 처리할 수 있도록 설정합니다.
     * </p>
     *
     * @param messageConverters 메시지 컨버터를 등록할 {@link List} 객체
     * @return 메시지 컨버터 구성이 완료되었음을 나타내는 {@code true}
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(new StringMessageConverter());
        // JSON 형식의 메시지를 변환하기 위해 Jackson 메시지 컨버터 추가
        messageConverters.add(mappingJackson2MessageConverter());

        return true;
    }
}