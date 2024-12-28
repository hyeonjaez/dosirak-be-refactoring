package com.example.dosirakbe.global.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.example.dosirakbe.global.config<br>
 * fileName       : JasyptConfig<br>
 * author         : femmefatalehaein<br>
 * date           : 10/27/24<br>
 * description    : Jasypt를 사용한 문자열 암호화 설정 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/27/24        femmefatalehaein                최초 생성<br>
 */
@Configuration
class JasyptConfig {

    /**
     * Jasypt 암호화에 사용되는 비밀번호입니다.
     *
     * <p>
     * 이 필드는 애플리케이션 설정 파일에 정의된 {@code jasypt.encryptor.password} 값을 참조합니다.
     * 암호화 및 복호화 작업에 사용됩니다.
     * </p>
     */
    @Value("${jasypt.encryptor.password}")
    private String password;

    /**
     * Jasypt 문자열 암호화기를 빈으로 등록합니다.
     *
     * <p>
     * 이 메서드는 {@link StringEncryptor} 인터페이스를 구현하는 {@link PooledPBEStringEncryptor}를 생성하여
     * 문자열 암호화와 복호화 작업에 사용할 수 있도록 Spring 애플리케이션 컨텍스트에 등록합니다.
     * </p>
     *
     * @return {@link StringEncryptor} 객체
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

    /**
     * Jasypt 암호화 테스트를 위한 메인 메서드입니다.
     *
     * <p>
     * 이 메서드는 {@link PooledPBEStringEncryptor}를 직접 구성하여 문자열을 암호화하고
     * 다시 복호화하는 과정을 콘솔에 출력합니다.
     * </p>
     *
     * @param args 실행 인자
     */
    public static void main(String[] args) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("gpudosirak");  // 테스트용 비밀번호 설정
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        String encrypted = encryptor.encrypt("admin");
        String decrypted = encryptor.decrypt(encrypted);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }

}