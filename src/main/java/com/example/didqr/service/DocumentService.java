package com.example.didqr.service;

import com.example.didqr.entity.Document;
import com.example.didqr.repository.DocumentRepository;
import com.example.didqr.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.didqr.util.QrUtil;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document saveDocument(String title, String issuerDid, String content) {
        String contentHash = HashUtil.sha256(content);

        Document document = new Document(title, issuerDid, contentHash);

        Document savedDocument = documentRepository.save(document);

        String qrPath = QrUtil.generateQrImage(savedDocument.getId());
        savedDocument.updateQrPath(qrPath);

        return documentRepository.save(savedDocument);
    }

    public Document findDocument(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));
    }

    public boolean verifyDocument(Long id, String inputContent) {
        Document document = findDocument(id);

        String inputHash = HashUtil.sha256(inputContent);

        return document.getContentHash().equals(inputHash);
    }

    public String getInputHash(String inputContent) {
        return HashUtil.sha256(inputContent);
    }
}