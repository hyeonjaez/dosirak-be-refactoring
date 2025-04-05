## 📦 Global Common Structure

이 프로젝트는 응답 일관성, 예외 처리, 유효성 검증을 위한 공통 유틸리티 및 응답 구조를 제공합니다.

---

### ✅ 공통 응답 구조

모든 API 응답은 `ApiResponse<T>`를 통해 일관된 형태로 반환됩니다.

```json
{
  "status": "SUCCESS",
  "message": "정상 처리되었습니다.",
  "data": {
    // 실제 응답 데이터
  }
}
```
### 📌 관련 클래스
- ApiResponse<T>: 공통 응답 포맷 (record 기반, 제네릭 지원)
- ponseEntityUtil: ResponseEntity<ApiResponse<>> 래핑 유틸 
- cessOk(), successCreated(), successResponseDataEmpty() 등 제공 
- EmptyResponse: 데이터가 없는 경우 단일 객체로 응답

---
### ⚠️ 공통 예외 처리

예외는 전역에서 GlobalExceptionHandler가 핸들링하며,
모든 예외는 ErrorResponseEntity 형식으로 응답됩니다.
```json
{
"statusEnum": "FAILURE",
"httpStatus": "BAD_REQUEST",
"code": "COMMON-001",
"message": "[email](은)는 필수 입력값입니다."
}
```

### 📌 관련 클래스
- CustomException: 비즈니스 예외를 표현하는 커스텀 런타임 예외
- ErrorCode: 예외 코드 Enum (HTTP 상태코드, 메시지, 고유 코드 포함)
- ErrorResponseEntity: 예외 응답 포맷
- GlobalExceptionHandler: 전역 예외 처리기
- 검증 실패, 미지원 메서드, 엔드포인트 없음 등 상황별 처리 제공

---

### 🧹 유효성 검증 유틸리티
- ObjectsUtil:
- checkAllNotNull(...): null 방지
- checkIdLongs(...), checkIdIntegers(...): ID 유효성 검사

예시 사용
```java
ObjectsUtil.checkIdLongs(userId, contentId);
```
---
### 🕒 BaseEntity
공통 생성일/수정일을 관리하는 @MappedSuperclass
```java
@CreatedDate private LocalDateTime createdAt;
@LastModifiedDate private LocalDateTime updatedAt;
```