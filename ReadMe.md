# DID QR Document Verifier

DID(Decentralized Identifier)와 QR 기반의 전자문서 진위 검증 흐름을 이해하기 위해 제작한 미니 프로젝트입니다.

문서 원본 자체를 저장하지 않고 SHA-256 해시를 생성하여 저장하며, QR 코드를 통해 사용자가 간편하게 문서 검증 페이지에 접근할 수 있도록 구성했습니다.

---

# 프로젝트 소개

전자문서 인증 시스템에서 사용되는 핵심 개념들을 직접 구현하고 흐름을 이해하는 것을 목표로 했습니다.

## 구현 목표

- DID 기반 발급자 식별
- SHA-256 기반 위변조 검증
- QR 기반 문서 검증 접근
- Spring Boot MVC 구조 이해
- 전자문서 인증 흐름 구현

---

# 주요 기능

## 1. 문서 등록

사용자가 문서를 등록하면:

```text
문서 내용 입력
→ SHA-256 해시 생성
→ DB 저장
→ QR 코드 생성
```

문서 원본 대신 해시값을 저장하여 위변조 여부를 검증합니다.

---

## 2. QR 코드 생성

문서 등록 시 검증 페이지 URL이 포함된 QR 코드가 생성됩니다.

예시:

```text
http://{server-ip}:8080/verify/{documentId}
```

모바일 기기로 QR을 스캔하여 검증 페이지에 접근할 수 있습니다.

---

## 3. 문서 위변조 검증

검증 과정:

```text
입력 문서 내용
→ SHA-256 해시 생성
→ 저장된 해시와 비교
→ 진본 여부 판별
```

문서 내용이 조금이라도 변경되면 해시값이 완전히 달라지는 SHA-256의 특성을 활용했습니다.

---

# 기술 스택

## Backend

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Thymeleaf

## 기타 라이브러리

- ZXing (QR 생성)
- SHA-256

## Blockchain (확장 예정)

- Solidity
- Smart Contract
- Document Hash Registry

---

# 프로젝트 구조

```text
src/main/java/com/example/didqr
│
├── controller
│   └── DocumentController
│
├── service
│   └── DocumentService
│
├── repository
│   └── DocumentRepository
│
├── entity
│   └── Document
│
└── util
    ├── HashUtil
    └── QrUtil
```

---

# 전체 동작 흐름

```text
문서 등록
→ SHA-256 해시 생성
→ DB 저장
→ QR 생성

QR 스캔
→ 검증 페이지 이동
→ 입력값 해시 생성
→ 저장된 해시와 비교
→ 진본 여부 확인
```

---

# DID 적용 방식

현재 프로젝트에서는 DID를 단순화하여 발급기관 식별자로 활용했습니다.

예시:

```text
did:example:issuer-001
```

실제 DID 환경에서는 DID Document, 공개키 검증, 전자서명 등을 추가하여 확장할 수 있습니다.

---

# 블록체인 확장 구조

현재 MVP에서는 DB 기반으로 구현했지만, 실제 서비스에서는 다음과 같이 확장 가능합니다.

```text
문서 내용
→ SHA-256 해시 생성
→ 스마트컨트랙트에 해시 저장
→ 검증 시 블록체인 해시 조회
→ 위변조 검증
```

블록체인에는 문서 원본이 아닌 해시값만 저장하여 비용 및 개인정보 문제를 최소화하는 구조를 고려했습니다.

---

# 실행 방법

## 프로젝트 실행

```bash
./gradlew bootRun
```

## 접속

```text
http://localhost:8080
```

---

# 학습한 내용

- SHA-256 기반 위변조 검증 구조
- QR 기반 인증 흐름
- Spring Boot MVC 구조
- JPA Entity 및 Repository 사용
- DID 및 전자문서 인증 개념 이해
- 블록체인 기반 문서 검증 구조 설계

---

# 향후 개선 예정

- PDF 파일 업로드 기반 검증
- Solidity 스마트컨트랙트 연동
- 전자서명 검증
- DID Document 구조 적용
- JWT 기반 사용자 인증
- IPFS 기반 문서 저장 구조