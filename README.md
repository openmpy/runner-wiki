# 런너위키

## 기술 스택

- Client
    - Typescript 5.9.2
    - Next.js 15
- Server
    - Java 21
    - Springboot 3.5.4
    - MySQL 8.0.38
    - Redis 7.4
    - Kafka 3.8.0
- Other
    - Docker
    - Docker Compose
    - Meilisearch 1.15
    - Vercel
    - Cloudflare
    - AWS S3

## 아키텍처

<img width="1257" height="827" alt="Image" src="https://github.com/user-attachments/assets/bcacd23a-50bd-417d-9ec3-a23784681444" />

## 트러블 슈팅

### 조회수 시스템 분리 및 비동기 처리

#### 문제 상황

기존 문서 조회 시스템에서는 조회수 증가 로직이 동기적으로 처리되어 다음과 같은 문제점들이 발생했습니다.

- 문서 조회 응답 속도 저하 (조회수 증가 처리로 인한 지연)
- 데이터베이스 부하 증가 (매 조회마다 즉시 DB 업데이트)
- 동시 접근 시 중복 조회수 증가 문제
- 조회수 처리 실패 시 문서 조회 자체가 실패하는 결합도 문제

#### 해결 방안

##### 1. 시스템 아키텍처 분리

조회수 처리를 별도 시스템으로 분리하여 문서 조회와 조회수 증가를 독립적으로 처리하도록 개선했습니다.

###### 기존 구조

```
문서 조회 → 조회수 증가 (동기) → 응답
```

###### 개선된 구조

```
문서 조회 → 응답 (빠른 응답)
     ↓
Kafka 이벤트 발행 → 비동기 조회수 처리
```

##### 2. Kafka를 활용한 비동기 이벤트 처리

```java
@KafkaListener(topics = "document-views", groupId = "view-counter")
public void consume(final String message) {
    try {
        final ViewEvent viewEvent = objectMapper.readValue(message, ViewEvent.class);
        viewService.incrementViewCount(viewEvent.documentId(), viewEvent.clientIp());
    } catch (final Exception e) {
        throw new CustomException("view event parsing error: " + e.getMessage());
    }
}
```

```java
public void viewDocument(final String documentId, final String clientIp) {
    final ViewEvent viewEvent = new ViewEvent(documentId, clientIp);
    try {
        final String json = objectMapper.writeValueAsString(viewEvent);
        kafkaTemplate.send("document-views", json);
    } catch (final JsonProcessingException e) {
        throw new CustomException("view document event json parsing error: " + e.getMessage());
    }
}
```

###### 장점

- 문서 조회 응답 속도 개선 (평균 200ms → 50ms)
- 시스템 간 느슨한 결합 구현
- 이벤트 처리 실패 시에도 문서 조회는 정상 동작

##### 3. Redis를 활용한 중복 방지 및 성능 최적화

```java
public void incrementViewCount(final String documentId, final String clientIp) {
    final String duplicatedKey = String.format(DOCUMENT_VIEW_DUPLICATION_KEY, documentId, clientIp);
    final String viewCountKey = String.format(DOCUMENT_VIEW_COUNT_KEY, documentId);
    final Boolean duplicated = redisTemplate.opsForValue().setIfAbsent(
            duplicatedKey, "happy", Duration.ofMinutes(10)
    );

    if (Boolean.TRUE.equals(duplicated)) {
        redisTemplate.opsForValue().increment(viewCountKey);
        redisTemplate.opsForZSet().incrementScore(DOCUMENT_VIEW_SCORE_KEY, documentId, 1.0);
    }
}
```

###### 핵심 기능

- 중복 방지: 동일 IP에서 10분 내 같은 문서 중복 조회 방지
- 캐시 활용: Redis 카운터로 빠른 조회수 처리
- 인기 문서 순위: ZSet을 활용한 실시간 인기 문서 랭킹

---

### JPA 페이지네이션 성능 최적화

#### 문제 상황

1천만 건의 Document 테이블에서 페이징 조회 시 4초가 소요되는 문제가 발생했습니다.

#### 문제 분석

##### 기존 JPA 방식의 문제점

JPA의 기본 Pageable을 사용한 페이지네이션은 다음과 같은 쿼리를 생성합니다.

```sql
SELECT d.id, d.title, d.category, d.status, d.created_at, d.updated_at
FROM document d
ORDER BY d.updated_at DESC LIMIT :limit
OFFSET :offset
```

###### 성능 문제 원인

1. OFFSET 성능 저하: 대용량 데이터에서 OFFSET이 클수록 데이터베이스는 건너뛸 행들을 모두 스캔해야 함
2. 정렬 오버헤드: 전체 테이블에 대한 정렬 작업 후 LIMIT/OFFSET 적용
3. 인덱스 미활용: updated_at 컬럼에 적절한 인덱스가 없을 경우 전체 테이블 스캔 발생

#### 해결 방안

##### 1. 서브쿼리를 활용한 네이티브 쿼리 최적화

```java

@Query(
        value = "SELECT d.id, d.title, d.category, d.status, d.created_at, d.updated_at " +
                "FROM (" +
                "  SELECT id FROM document " +
                "  ORDER BY updated_at DESC " +
                "  LIMIT :limit OFFSET :offset" +
                ") t LEFT JOIN document d ON t.id = d.id",
        nativeQuery = true
)
List<Document> findAllOrderByUpdatedAtDesc(
        @Param("offset") final int offset,
        @Param("limit") final int limit
);
```

###### 최적화 포인트

- 서브쿼리 활용: 먼저 필요한 id만 빠르게 조회
- 인덱스 최적화: updated_at에 대한 인덱스 활용
- 데이터 전송량 감소: 서브쿼리에서는 id만 조회하여 I/O 최소화

##### 2. Count 쿼리 최적화

```java

@Query(
        value = "SELECT count(*) " +
                "FROM (" +
                "  SELECT id FROM document " +
                "  LIMIT :limit" +
                ") t",
        nativeQuery = true
)
Long count(@Param("limit") final int limit);
```

##### 3. 인덱스 생성

```sql
CREATE INDEX idx_document_updated_at ON document (updated_at DESC);
```

#### 개선 결과

- 기존 성능: JPA 기본 페이지네이션 기능 사용 시 4초 소요
- 개선 후 성능: 커스텀 네이티브 쿼리 + 인덱스 적용 후 0.2초 (95% 성능 향상)

---

### Redis + AOP를 활용한 중복 요청 방지

#### 문제 상황

이미지 업로드 시 사용자가 동시에 여러 번 버튼을 클릭하면 중복으로 업로드되는 현상이 발생했습니다.

#### 문제 분석

1. 프론트엔드 중복 클릭: 사용자가 응답을 기다리지 않고 연속으로 버튼 클릭
2. 서버 측 중복 처리 방지 부재: 동일한 요청에 대한 검증 로직 없음
3. 비동기 처리: 업로드 처리 중에도 새로운 요청이 계속 들어오는 구조

#### 구현 전략

##### 1. 커스텀 어노테이션 정의

```java

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDuplicate {
    String keyExpression() default "";

    int timeoutSeconds() default 10;
}
```

##### 2. AOP Aspect 구현

###### 락 메커니즘

```java
final Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(
        key, "processing", Duration.ofSeconds(preventDuplicate.timeoutSeconds())
);
```

- setIfAbsent: Redis의 원자적 연산으로 동시성 보장
- 키가 존재하지 않을 때만 설정하여 첫 번째 요청만 처리

###### 키 생성 전략

```java
private String generateKey(final ProceedingJoinPoint joinPoint, final String keyExpression) {
    if (keyExpression.isEmpty()) {
        return joinPoint.getTarget().getClass().getSimpleName() + "::" + joinPoint.getSignature().getName();
    }
    return parseSpelExpression(joinPoint, keyExpression);
}
```

###### SpEL 표현식 파싱

```java

@PreventDuplicate(keyExpression = "'image-upload' + '::' + "
        + "T(com.openmpy.wiki.global.utils.ClientIpExtractor).getClientIp(#servletRequest) + '::' + "
        + "#file.originalFilename + '::' + #file.size"
)
@PostMapping
public ResponseEntity<ImageUploadResponse> uploadImage(
        final MultipartFile file,
        final HttpServletRequest servletRequest
) {
    final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
    return ResponseEntity.ok(imageService.upload(file, clientIp));
}
```

- 메서드 파라미터를 기반으로 동적 키 생성
- 사용자별, 파일별 세밀한 중복 검사 가능

#### 개선 결과

- 중복 업로드 방지율: 100%
- Redis 응답 시간: 평균 1-2ms
