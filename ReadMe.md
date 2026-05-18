# DID QR Document Verifier

DID(Decentralized Identifier)와 QR 기반의 전자문서 진위 검증 흐름을 이해하기 위해 제작한 미니 프로젝트입니다.

문서 원본 자체를 저장하지 않고 SHA-256 해시를 생성하여 저장하며, 생성된 해시는 스마트컨트랙트를 통해 로컬 블록체인(Ganache)에 등록됩니다.

QR 코드를 통해 사용자가 간편하게 문서 검증 페이지에 접근할 수 있도록 구성했습니다.

---

# 프로젝트 소개

전자문서 인증 시스템에서 사용되는 핵심 개념들을 직접 구현하고 전체 인증 흐름을 이해하는 것을 목표로 했습니다.

## 구현 목표

* DID 기반 발급자 식별
* SHA-256 기반 위변조 검증
* QR 기반 문서 검증 접근
* Spring Boot MVC 구조 이해
* Solidity 스마트컨트랙트 연동
* 블록체인 기반 해시 검증 구조 구현

---

# 주요 기능

## 1. 문서 등록

사용자가 문서를 등록하면:

```text
문서 내용 입력 / PDF 파일 업로드
→ SHA-256 해시 생성
→ DB 저장
→ 스마트컨트랙트 registerDocument() 호출
→ 블록체인 해시 저장
→ txHash 저장
→ QR 코드 생성
```

문서 원본 대신 SHA-256 해시만 저장하여 위변조 여부를 검증합니다.

Spring Boot가 Web3j를 통해 스마트컨트랙트를 호출하여 블록체인에 문서 해시를 등록합니다.

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
입력 문서 내용 또는 PDF 업로드
→ SHA-256 해시 생성
→ 스마트컨트랙트 getDocument() 호출
→ 블록체인 해시 조회
→ 업로드 파일 해시와 비교
→ 진본 여부 판별
```

문서 내용이 조금이라도 변경되면 해시값이 완전히 달라지는 SHA-256의 특성을 활용했습니다.

---

# 기술 스택

## Backend

* Java 17
* Spring Boot
* Spring Data JPA
* H2 Database
* Thymeleaf

## Blockchain

* Solidity
* Ganache
* Web3j
* Smart Contract
* Ethereum RPC

## 기타 라이브러리

* ZXing (QR 생성)
* SHA-256

---

# 프로젝트 구조

```text
src/main/java/com/example/didqr
│
├── config
│   └── Web3Config
│
├── controller
│   └── DocumentController
│
├── service
│   ├── DocumentService
│   └── BlockchainService
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
→ 스마트컨트랙트 registerDocument()
→ 블록체인 해시 저장
→ txHash 저장
→ QR 생성

QR 스캔
→ 검증 페이지 이동
→ 업로드 파일 해시 생성
→ 스마트컨트랙트 getDocument()
→ 블록체인 해시 조회
→ 업로드 파일 해시와 비교
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

# 블록체인 연동 구조

문서 등록 시 Spring Boot가 Web3j를 통해 스마트컨트랙트를 호출하여 문서 해시를 로컬 Ethereum 네트워크(Ganache)에 저장합니다.

블록체인에는 문서 원본이 아닌 SHA-256 해시만 저장하며, 검증 시 블록체인에 저장된 해시와 업로드 파일의 해시를 비교하여 위변조 여부를 판별합니다.

---

# 실행 방법

## 1. Ganache 실행

Ganache 로컬 블록체인을 실행합니다.

RPC 예시:

```text
http://127.0.0.1:7545
```

---

## 2. 스마트컨트랙트 배포

Remix IDE를 통해 Solidity 스마트컨트랙트를 Ganache 네트워크에 배포합니다.

배포 후:

* Contract Address
* Private Key
* RPC URL

을 Spring 설정에 등록합니다.

---

## 3. application.properties 설정

```properties
blockchain.rpc-url=http://127.0.0.1:7545
blockchain.private-key=YOUR_PRIVATE_KEY
blockchain.contract-address=YOUR_CONTRACT_ADDRESS
```

---

## 4. 프로젝트 실행

```bash
./gradlew bootRun
```

---

## 5. 접속

```
http://localhost:8080

```

# 향후 개선 예정

* 실제 Ethereum Testnet 연동
* DID Document 공개키 검증
* 전자서명 기반 VC 검증
* JWT 기반 사용자 인증
* IPFS 기반 분산 저장
* MetaMask 사용자 서명 연동
* 블록체인 Event 기반 검증 로그 추적
