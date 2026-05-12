package com.example.didqr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String issuerDid;

    @Lob
    private String contentHash;

    private LocalDateTime issuedAt;

    private String qrPath;

    public void updateQrPath(String qrPath) {
        this.qrPath = qrPath;
    }

    public Document(String title, String issuerDid, String contentHash) {
        this.title = title;
        this.issuerDid = issuerDid;
        this.contentHash = contentHash;
        this.issuedAt = LocalDateTime.now();
    }
}