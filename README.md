# Hexagonal Architecture 연습 프로젝트

Spring Boot로 헥사고날 아키텍처(Port & Adapter 패턴)를 직접 뜯어보고 구현하며 학습한 내용을 정리한다.

---

## 1. 헥사고날 아키텍처란?

도메인(핵심 비즈니스 로직)을 육각형 상자로 두고, 외부 세계와 연결되는 지점마다 **Port(구멍)** 를 뚫어두는 구조.
그 Port에 실제로 꽂히는 구현체가 **Adapter(플러그)** 다.

- **Port** = 인터페이스. "무엇을 할 수 있는지 / 무엇이 필요한지"의 규격만 정의하고, 구현은 없음.
- **Adapter** = Port를 실제로 구현한 클래스. 진짜 기술(JPA, REST 등)이 여기에 담김.

### Port In vs Port Out

| 구분 | Port In | Port Out |
|---|---|---|
| 방향 | 외부 → 도메인 (요청이 들어옴) | 도메인 → 외부 (요청이 나감) |
| 역할 | 도메인이 제공하는 기능 정의 (UseCase) | 도메인이 필요로 하는 기능 정의 (Repository 등) |
| 실제 구현체 위치 | `adapter.in.web` (Controller가 호출) | `adapter.out.persistence` (JPA가 구현) |

### 왜 이렇게 나누는가

- Service는 `PostRepository`라는 **인터페이스**만 알고, 그게 JPA로 구현됐는지 MongoDB로 구현됐는지 모른다.
- 나중에 저장 기술을 바꾸고 싶으면 Adapter만 새로 짜면 되고, **Service(도메인 로직)는 한 줄도 안 건드려도 된다.**
- 이게 의존성 역전(DIP) — 도메인이 프레임워크나 기술에 의존하지 않고, 오히려 기술이 도메인의 인터페이스에 맞춰 구현된다.

---

## 2. 패키지 구조

```
com.example.hexagonalpostapi
├── domain                          # 순수 자바 객체(POJO), 프레임워크 의존 없음
│   └── Post
├── application
│   ├── port.in                     # 도메인이 제공하는 기능 (Port In)
│   │   ├── CreatePostUseCase
│   │   ├── GetPostUseCase
│   │   ├── GetPostListUseCase
│   │   └── SearchPostUseCase
│   ├── port.out                    # 도메인이 필요로 하는 기능 (Port Out)
│   │   └── PostRepository
│   ├── service                     # Port In의 구현체 (실제 비즈니스 로직)
│   │   ├── PostService             # 생성(Command) 담당
│   │   └── PostQueryService        # 조회(Query) 담당 — 쓰기/읽기 책임 분리
│   └── exception                   # 비즈니스 예외 (HTTP를 모름)
│       └── PostNotFoundException
└── adapter
    ├── in.web                      # Port In의 호출자 (HTTP 진입점)
    │   ├── PostController
    │   ├── CreatePostRequest
    │   ├── PostResponse
    │   ├── PostWebMapper            # 도메인 ↔ 응답 DTO 변환 전담
    │   ├── GlobalExceptionHandler    # 예외 → HTTP 상태코드 변환
    │   └── ErrorResponse
    └── out.persistence             # Port Out의 구현체 (JPA + QueryDSL 기술)
        ├── PostJpaEntity
        ├── PostJpaRepository        # Spring Data JPA + Custom(QueryDSL) 상속
        ├── PostJpaCustomRepository       # QueryDSL 커스텀 조회 인터페이스
        ├── PostJpaCustomRepositoryImpl   # QueryDSL 실제 구현
        ├── QueryDslConfig           # JPAQueryFactory Bean 등록
        └── PostPersistenceAdapter
```

---

## 3. 요청 흐름: Controller → JPA 저장까지 (생성 API 예시)

```mermaid
sequenceDiagram
    participant Client
    participant PostController as PostController<br/>(adapter.in.web)
    participant UseCase as CreatePostUseCase<br/>(Port In, interface)
    participant PostService as PostService<br/>(application.service)
    participant Repository as PostRepository<br/>(Port Out, interface)
    participant Adapter as PostPersistenceAdapter<br/>(adapter.out.persistence)
    participant JpaRepo as PostJpaRepository<br/>(Spring Data JPA)
    participant DB as H2 Database

    Client->>PostController: POST /api/posts<br/>{title, content}
    PostController->>UseCase: createPost(title, content)
    Note over UseCase,PostService: PostService가 UseCase를 구현<br/>(Spring이 자동 연결)
    UseCase->>PostService: (실제 실행)
    PostService->>PostService: Post.create(title, content)<br/>도메인 객체 생성
    PostService->>Repository: save(post)
    Note over Repository,Adapter: PostPersistenceAdapter가<br/>Repository를 구현
    Repository->>Adapter: (실제 실행)
    Adapter->>Adapter: 도메인 Post → PostJpaEntity 변환
    Adapter->>JpaRepo: save(entity)
    JpaRepo->>DB: INSERT INTO posts ...
    DB-->>JpaRepo: 저장된 row
    JpaRepo-->>Adapter: PostJpaEntity (id 채워짐)
    Adapter->>Adapter: PostJpaEntity → 도메인 Post 변환
    Adapter-->>PostService: Post
    PostService-->>PostController: Post
    PostController->>PostController: PostWebMapper.toResponse(post)
    PostController-->>Client: PostResponse (JSON)
```

### 클래스/인터페이스 대응표

| 계층 | 이름 | 역할 |
|---|---|---|
| 도메인 | `Post` | 순수 자바 객체(POJO), 비즈니스 개념 |
| Port In | `CreatePostUseCase` | "게시글을 생성할 수 있다"는 규격 (인터페이스) |
| Port In | `GetPostUseCase` | "id로 단건 조회할 수 있다"는 규격 |
| Port In | `GetPostListUseCase` | "전체 목록을 조회할 수 있다"는 규격 |
| Port In | `SearchPostUseCase` | "제목으로 검색할 수 있다"는 규격 |
| Service | `PostService` | `CreatePostUseCase` 구현체, 생성 로직 처리 |
| Service | `PostQueryService` | `GetPostUseCase` / `GetPostListUseCase` / `SearchPostUseCase` 구현체, 조회 로직 처리 |
| Port Out | `PostRepository` | "저장/조회할 수 있어야 한다"는 규격 (인터페이스) |
| Adapter (in) | `PostController` | HTTP 요청을 받아 각 UseCase 호출 |
| Adapter (in) | `PostWebMapper` | 도메인 `Post` → `PostResponse` 변환 전담 |
| Adapter (in) | `GlobalExceptionHandler` | 비즈니스 예외를 HTTP 응답으로 변환 |
| Adapter (out) | `PostPersistenceAdapter` | `PostRepository` 구현체, 도메인 ↔ JPA 엔티티 변환 담당 |
| 기술 상세 | `PostJpaEntity` | `@Entity`가 붙은 실제 JPA 엔티티 (도메인 `Post`와 분리) |
| 기술 상세 | `PostJpaRepository` | Spring Data JPA + QueryDSL Custom 인터페이스 상속 |
| 기술 상세 | `PostJpaCustomRepositoryImpl` | QueryDSL로 짠 동적 쿼리 실제 구현 |

**핵심 포인트:** Controller는 각 UseCase 인터페이스만 알고, `PostService`/`PostQueryService`라는 구체 클래스의 존재를 모른다.
마찬가지로 Service도 `PostRepository` 인터페이스만 알고, `PostPersistenceAdapter`나 JPA/QueryDSL의 존재를 모른다.
양쪽 다 Spring이 런타임에 자동으로 연결(의존성 주입)해준다.

---

## 4. 왜 domain.Post 와 PostJpaEntity를 따로 두는가

지금은 필드가 완전히 똑같아서 낭비처럼 보이지만:

- 도메인 `Post`는 **비즈니스 로직**을 담는 곳 (프레임워크 의존 없음)
- `PostJpaEntity`는 **DB 테이블과 매핑**되는 기술적인 표현

나중에 도메인 로직이 복잡해지거나(예: `Post`에 검증/계산 메서드 추가), DB 테이블 구조와 도메인 개념이 달라지기 시작하면
이 둘을 분리해둔 게 진가를 발휘한다. `PostPersistenceAdapter`가 그 변환을 전담한다.

---

## 5. Mapper 패턴 (응답 변환 중복 제거)

생성/단건조회/목록조회/검색 API 모두 `Post` → `PostResponse` 변환이 필요한데, 이걸 각 메서드마다 반복하지 않고
`PostWebMapper.toResponse(post)`로 한 곳에 모았다.

- 위치: `adapter.in.web` — "도메인 객체를 HTTP 응답으로 어떻게 표현할지"는 웹 어댑터만의 관심사이기 때문.
- 도메인은 자신이 HTTP로 어떻게 보여지는지 전혀 몰라야 한다.
- 참고: `PostPersistenceAdapter` 내부의 `Post` ↔ `PostJpaEntity` 변환도 같은 성격의 관심사. 코드가 커지면 `PostPersistenceMapper`로 분리 가능 (현재는 메서드 하나뿐이라 보류).

---

## 6. QueryDSL 설정 (제목 검색 기능)

### build.gradle

```gradle
dependencies {
    // QueryDSL
    implementation 'com.querydsl:querydsl-jpa:5.1.0:jakarta'
    annotationProcessor 'com.querydsl:querydsl-apt:5.1.0:jakarta'
    annotationProcessor 'jakarta.annotation:jakarta.annotation-api'
    annotationProcessor 'jakarta.persistence:jakarta.persistence-api'
}

// Lombok 등 다른 annotationProcessor와 충돌 방지
configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}
```

- `:jakarta` 접미사 필수 (Spring Boot 3.x+ 는 `javax` → `jakarta` 네임스페이스로 이전됨. 빠뜨리면 `NoClassDefFoundError`)
- 빌드하면 `build/generated/sources/annotationProcessor/java/main`에 `QPostJpaEntity` 같은 Q클래스가 자동 생성됨

### 구조

- `QueryDslConfig` — `JPAQueryFactory`를 Bean으로 등록
- `PostJpaCustomRepository` — QueryDSL로 구현할 커스텀 조회 메서드 규격 (인터페이스)
- `PostJpaCustomRepositoryImpl` — 실제 QueryDSL 코드 (`QPostJpaEntity`의 static 인스턴스로 타입 안전하게 조건 작성)
- `PostJpaRepository extends JpaRepository<...>, PostJpaCustomRepository` — 기본 CRUD(Spring Data JPA 자동 구현) + 커스텀 조회(QueryDSL) 를 하나의 인터페이스로 합침

### API

```
GET /api/posts/search?keyword=검색어
```

쿼리 파라미터 방식 채택. (참고: 검색 조건이 복잡해지면 `POST /api/posts/search` + body 형태도 실무에서 종종 쓰는 패턴 — 지금은 keyword 하나뿐이라 GET이 표준적)

---

## 7. 레이어별 예외처리

헥사고날 원칙상 **도메인/애플리케이션 계층은 HTTP를 몰라야 한다.** 그래서 예외 정의와 예외→HTTP 변환 책임을 분리한다.

| 계층 | 역할 |
|---|---|
| `application.exception` | 비즈니스 예외 정의 & 발생 (`PostNotFoundException`) — HTTP 상태코드 개념 없음 |
| `adapter.in.web` | `@RestControllerAdvice`로 예외를 잡아서 HTTP 상태코드로 변환 |

### 흐름

```
PostQueryService.getPost(id)
  → postRepository.findById(id) 결과가 없으면
  → PostNotFoundException 발생 (application 계층, RuntimeException 상속)
       ↓
GlobalExceptionHandler.handlePostNotFound() 가 잡아서
  → 404 Not Found + ErrorResponse(status, message) 로 변환
```

- `PostNotFoundException`은 `RuntimeException`을 상속 (Checked Exception으로 만들면 모든 Port 인터페이스 시그니처에 `throws`가 번져서 인터페이스가 오염됨)
- `GlobalExceptionHandler`는 `PostController`뿐 아니라 프로젝트의 모든 Controller에 공통 적용됨 — Controller 코드에는 try-catch가 하나도 없음

---

## 8. 다음 학습 예정

- [x] 조회 API 추가 (`GET /api/posts/{id}`, `GET /api/posts`)
- [x] QueryDSL로 동적 쿼리 붙이기 (제목 검색)
- [x] Mapper 패턴으로 응답 변환 중복 제거
- [x] 레이어별 예외처리 (`PostNotFoundException` + `GlobalExceptionHandler`)
- [ ] 이벤트 발행 구조 (`ApplicationEventPublisher` → 추후 Kafka 등으로 확장)
- [ ] JWT 인증 붙이기
- [ ] JPA Adapter를 다른 기술(MongoDB 등)로 교체해보기 — Port/Adapter 분리 효과 체감용, 구조가 손에 익은 뒤 마지막 단계로 진행