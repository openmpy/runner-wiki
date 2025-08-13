# 런너위키

## 기술 스택
- Client
  - Typescript
  - Next.js
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

## 시스템 아키텍처

```mermaid
graph TB
    User[사용자] --> CF1[Cloudflare CDN]
    
    subgraph "클라이언트"
        CF1 --> Vercel[Vercel]
        Vercel --> NextJS[Next.js App]
    end
    
    subgraph "서버"
        NextJS --> CF2[Cloudflare]
        CF2 --> Lightsail[AWS Lightsail]
        Lightsail --> SpringBoot[Spring Boot API]
    end
    
    subgraph "조회수 처리 시스템"
        SpringBoot --> Kafka[Apache Kafka]
        Kafka --> KafkaConsumer[Kafka Consumer]
        KafkaConsumer --> Redis[Redis Cache]
        Redis --> MySQL[MySQL DB]
    end
    
    subgraph "데이터베이스"
        SpringBoot --> MySQL
        MySQL --> Document[문서 데이터]
        MySQL --> ViewCount[조회수 데이터]
        MySQL --> Image[이미지 데이터]
    end
```
