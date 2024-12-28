package com.example.dosirakbe.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.example.dosirakbe.global.config<br>
 * fileName       : S3Config<br>
 * author         : femmefatalehaein<br>
 * date           : 10/26/24<br>
 * description    : Amazon S3 클라이언트 설정 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/26/24        femmefatalehaein                최초 생성<br>
 */
@Configuration
public class S3Config {

    /**
     * AWS Access Key 입니다.
     *
     * <p>
     * 이 필드는 애플리케이션 설정 파일에서 {@code cloud.aws.credentials.accessKey} 값을 읽어옵니다.
     * Amazon S3 클라이언트 인증에 사용됩니다.
     * </p>
     */
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    /**
     * AWS Secret Key 입니다.
     *
     * <p>
     * 이 필드는 애플리케이션 설정 파일에서 {@code cloud.aws.credentials.secretKey} 값을 읽어옵니다.
     * Amazon S3 클라이언트 인증에 사용됩니다.
     * </p>
     */
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    /**
     * AWS S3 리전 설정입니다.
     *
     * <p>
     * 이 필드는 애플리케이션 설정 파일에서 {@code cloud.aws.region.static} 값을 읽어옵니다.
     * Amazon S3 클라이언트의 기본 리전을 설정하는 데 사용됩니다.
     * </p>
     */
    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * Amazon S3 클라이언트를 생성하고 빈으로 등록합니다.
     *
     * <p>
     * 이 메서드는 AWS 자격 증명과 리전 설정을 사용하여 {@link AmazonS3} 클라이언트를 생성합니다.
     * 이를 통해 애플리케이션에서 Amazon S3 API 를 호출할 수 있습니다.
     * </p>
     *
     * <p><strong>사용 예:</strong></p>
     * <pre>{@code
     * AmazonS3 s3Client = amazonS3();
     * s3Client.putObject("bucket-name", "file-key", "file-content");
     * }</pre>
     *
     * @return {@link AmazonS3} 객체
     */
    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

}