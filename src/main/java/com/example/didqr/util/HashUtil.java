package com.example.didqr.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public class HashUtil {

    // 텍스트 해시화
    public static String sha256(String content) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("해시 생성 실패");
        }
    }

    // 파일 해시화
    public static String sha256(MultipartFile file) {
        try {

            // pdf 파일만 허용
            /*
                if (!file.getContentType().equals("application/pdf")) {
                throw new RuntimeException("PDF만 업로드 가능합니다.");
            }
            */

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(file.getBytes());

            return toHex(hash);

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("파일 해시 생성 실패", e);
        }
    }

    private static String toHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }
}