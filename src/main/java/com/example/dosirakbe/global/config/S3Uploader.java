package com.example.dosirakbe.global.config;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.dosirakbe.global.util.CustomException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * packageName    : com.example.dosirakbe.global.config<br>
 * fileName       : S3Uploader<br>
 * author         : femmefatalehaein<br>
 * date           : 10/26/24<br>
 * description    : Amazon S3에 파일 업로드 및 삭제를 처리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/26/24        femmefatalehaein                최초 생성<br>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private Set<String> uploadedFileNames = new HashSet<>();
    private Set<Long> uploadedFileSizes = new HashSet<>();

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;


    /**
     * 여러 개의 파일을 S3에 업로드합니다.
     *
     * <p>
     * 중복된 파일은 업로드되지 않으며, 중복이 확인되면 {@link ExceptionEnum#DUPLICATE_IMAGE} 예외가 발생합니다.
     * 업로드가 완료된 파일의 S3 URL 목록을 반환합니다.
     * </p>
     *
     * @param multipartFiles 업로드할 {@link MultipartFile} 목록
     * @return 업로드된 파일의 S3 URL 목록
     * @throws CustomException 중복된 파일이 포함되어 있거나 업로드 중 오류가 발생한 경우
     */
    public List<String> saveFiles(List<MultipartFile> multipartFiles) {
        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {

            if (isDuplicate(multipartFile)) {
                throw new CustomException(ExceptionEnum.DUPLICATE_IMAGE);
            }

            String uploadedUrl = saveFile(multipartFile);
            uploadedUrls.add(uploadedUrl);
        }

        clear();
        return uploadedUrls;
    }

    /**
     * S3에 저장된 파일을 삭제합니다.
     *
     * <p>
     * 파일 URL 에서 버킷 이름과 파일 키를 추출하여 삭제 작업을 수행합니다.
     * 파일이 존재하지 않거나 삭제 실패 시 적절한 {@link CustomException}이 발생합니다.
     * </p>
     *
     * @param fileUrl 삭제할 파일의 S3 URL
     * @throws CustomException 파일이 존재하지 않거나 삭제 중 오류가 발생한 경우
     */
    public void deleteFile(String fileUrl) {
        String[] urlParts = fileUrl.split("/");
        String fileBucket = urlParts[2].split("\\.")[0];

        if (!fileBucket.equals(bucket)) {
            throw new CustomException(ExceptionEnum.NO_IMAGE_EXIST);

        }

        String objectKey = String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length));

        if (!amazonS3.doesObjectExist(bucket, objectKey)) {
            throw new CustomException(ExceptionEnum.NO_IMAGE_EXIST);
        }

        try {
            amazonS3.deleteObject(bucket, objectKey);
        } catch (AmazonS3Exception e) {
            log.error("File delete fail : " + e.getMessage());
            throw new CustomException(ExceptionEnum.FAIL_DELETE);

        } catch (SdkClientException e) {
            log.error("AWS SDK client error : " + e.getMessage());
            throw new CustomException(ExceptionEnum.FAIL_DELETE);
        }

        log.info("File delete complete: " + objectKey);
    }

    /**
     * 단일 파일을 S3에 업로드합니다.
     *
     * <p>
     * 랜덤 파일 이름을 생성하여 S3에 업로드하며, 업로드가 완료되면 해당 파일의 S3 URL 을 반환합니다.
     * </p>
     *
     * @param file 업로드할 {@link MultipartFile}
     * @return 업로드된 파일의 S3 URL
     * @throws CustomException 업로드 중 오류가 발생한 경우
     */
    public String saveFile(MultipartFile file) {
        String randomFilename = generateRandomFilename(file);

        log.info("File upload started: " + randomFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(bucket, randomFilename, file.getInputStream(), metadata);
        } catch (AmazonS3Exception e) {
            log.error("Amazon S3 error while uploading file: " + e.getMessage());
            throw new CustomException(ExceptionEnum.FAIL_UPLOAD);
        } catch (SdkClientException e) {
            log.error("AWS SDK client error while uploading file: " + e.getMessage());
            throw new CustomException(ExceptionEnum.FAIL_UPLOAD);
        } catch (IOException e) {
            log.error("IO error while uploading file: " + e.getMessage());
            throw new CustomException(ExceptionEnum.FAIL_UPLOAD);
        }

        log.info("File upload completed: " + randomFilename);

        return amazonS3.getUrl(bucket, randomFilename).toString();
    }

    /**
     * 요청된 파일이 중복되었는지 확인합니다.
     *
     * <p>
     * 파일 이름과 크기를 기준으로 중복 여부를 판단합니다.
     * </p>
     *
     * @param multipartFile 중복 여부를 확인할 {@link MultipartFile}
     * @return 중복된 파일이면 {@code true}, 그렇지 않으면 {@code false}
     */
    private boolean isDuplicate(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        Long fileSize = multipartFile.getSize();

        if (uploadedFileNames.contains(fileName) && uploadedFileSizes.contains(fileSize)) {
            return true;
        }

        uploadedFileNames.add(fileName);
        uploadedFileSizes.add(fileSize);

        return false;
    }

    /**
     * 중복 확인에 사용된 데이터(파일 이름, 크기)를 초기화합니다.
     */
    private void clear() {
        uploadedFileNames.clear();
        uploadedFileSizes.clear();
    }

    /**
     * 랜덤 파일 이름을 생성합니다.
     *
     * <p>
     * 파일 이름 중복을 방지하기 위해 UUID 를 사용하여 랜덤 파일 이름을 생성합니다.
     * </p>
     *
     * @param multipartFile 파일 이름을 생성할 {@link MultipartFile}
     * @return 생성된 랜덤 파일 이름
     * @throws CustomException 허용되지 않은 확장자인 경우
     */
    private String generateRandomFilename(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = validateFileExtension(originalFilename);
        return UUID.randomUUID() + "." + fileExtension;
    }

    /**
     * 파일 확장자를 검증합니다.
     *
     * <p>
     * 허용된 확장자가 아니면 {@link ExceptionEnum#NOT_IMAGE_EXTENSION} 예외를 발생시킵니다.
     * </p>
     *
     * @param originalFilename 검증할 파일 이름
     * @return 검증된 확장자
     * @throws CustomException 허용되지 않은 확장자인 경우
     */
    private String validateFileExtension(String originalFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "jpeg");

        if (!allowedExtensions.contains(fileExtension)) {
            throw new CustomException(ExceptionEnum.NOT_IMAGE_EXTENSION);
        }
        return fileExtension;
    }
}