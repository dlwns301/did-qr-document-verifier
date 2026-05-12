package com.example.didqr.service;

import com.example.didqr.entity.Document;
import com.example.didqr.repository.DocumentRepository;
import com.example.didqr.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.didqr.util.QrUtil;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    // 문서 or 파일 저장
    public Document saveDocument(String title, String issuerDid, String content, MultipartFile file) {
        String contentHash;

        if (file != null && !file.isEmpty()) {
            contentHash = HashUtil.sha256(file);
        } else {
            contentHash = HashUtil.sha256(content);
        }

        Document document = new Document(title, issuerDid, contentHash);

        Document savedDocument = documentRepository.save(document);

        String qrPath = QrUtil.generateQrImage(savedDocument.getId());
        savedDocument.updateQrPath(qrPath);

        return documentRepository.save(savedDocument);
    }

    // 문서 검색
    public Document findDocument(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));
    }

    // 문서 검증
    public boolean verifyDocument(Long id, String inputContent) {
        Document document = findDocument(id);

        String inputHash = HashUtil.sha256(inputContent);

        return document.getContentHash().equals(inputHash);
    }

    // 해시화
    public String getInputHash(String inputContent) {
        return HashUtil.sha256(inputContent);
    }

    // 파일 검증
    public boolean verifyDocumentFile(Long id, MultipartFile file) {
        Document document = findDocument(id);

        String fileHash = HashUtil.sha256(file);

        return document.getContentHash().equals(fileHash);
    }

    // 파일 해시화
    public String getFileHash(MultipartFile file) {
        return HashUtil.sha256(file);
    }
}