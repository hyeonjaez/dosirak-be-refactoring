# 1. OpenJDK 베이스 이미지 사용
FROM openjdk:17

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 애플리케이션 JAR 파일을 컨테이너에 복사
COPY build/libs/dosirak-be-0.0.1-SNAPSHOT.jar app.jar

# 4. 컨테이너에서 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]