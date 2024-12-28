package com.example.dosirakbe.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * packageName    : com.example.dosirakbe.global.config<br>
 * fileName       : JpaAuditingConfig<br>
 * author         : Fiat_lux<br>
 * date           : 12/24/24<br>
 * description    : JPA Auditing 활성화 하는 설정 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 12/24/24        Fiat_lux                최초 생성<br>
 */
@Configuration
@EnableJpaAuditing
@Profile("!test")
public class JpaAuditingConfig {
}