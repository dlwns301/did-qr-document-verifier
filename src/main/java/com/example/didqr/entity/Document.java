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

    // 블록체인 txid
    @Column
    private String txHash;

    // 블록체인 저장 상태
    @Column
    private Boolean blockchainRegistered = false;

    public void updateQrPath(String qrPath) {
        this.qrPath = qrPath;
    }

    public Document(String title, String issuerDid, String contentHash) {
        this.title = title;
        this.issuerDid = issuerDid;
        this.contentHash = contentHash;
        this.issuedAt = LocalDateTime.now();
    }

    public void updateBlockchainInfo(String txHash) {
        this.txHash = txHash;
        this.blockchainRegistered = true;
    }
}