# DID QR Document Verifier

DID(Decentralized Identifier)와 QR 기반의 전자문서 진위 검증 흐름을 이해하기 위해 제작한 미니 프로젝트입니다.

문서 원본 자체를 저장하지 않고 SHA-256 해시를 생성하여 저장하며, QR 코드를 통해 사용자가 간편하게 문서 검증 페이지에 접근할 수 있도록 구성했습니다.

---

# 프로젝트 목적

전자문서 인증 시스템에서 사용되는 다음 개념들을 직접 구현하고 흐름을 이해하는 것을 목표로 했습니다.

- DID 기반 발급자 식별
- SHA-256 해시 기반 위변조 검증
- QR 기반 문서 검증 접근
- 전자문서 인증 흐름
- Spring Boot 기반 REST 구조 이해

---

# 기술 스택

## Backend

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Thymeleaf

## Blockchain (예정 구조)

- Solidity
- Smart Contract
- Document Hash Registry

## 기타

- ZXing (QR 생성)
- SHA-256

---

# 주요 기능

## 1. 문서 등록

사용자가 문서를 등록하면:

```text
문서 내용
→ SHA-256 해시 생성
→ 문서 정보 저장
→ QR 코드 생성