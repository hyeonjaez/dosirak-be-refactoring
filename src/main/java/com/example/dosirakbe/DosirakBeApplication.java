package com.example.dosirakbe;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * packageName    : com.example.dosirakbe<br>
 * fileName       : DosirakBeApplication<br>
 * author         : femmefatalehaein<br>
 * date           : 10/13/24<br>
 * description    : Dosirak 프로젝트의 Spring Boot 애플리케이션 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/13/24        femmefatalehaein                최초 생성<br>
 */
@EnableScheduling
@EnableCaching
@SpringBootApplication
public class DosirakBeApplication {

    /**
     * 애플리케이션의 진입점 메서드입니다.
     *
     * <p>
     * Spring Boot 애플리케이션을 시작하기 위해 {@link SpringApplication#run(Class, String...)} 메서드를 호출합니다.
     * </p>
     *
     * @param args 명령줄 인자
     */
    public static void main(String[] args) {
        SpringApplication.run(DosirakBeApplication.class, args);
    }

}
