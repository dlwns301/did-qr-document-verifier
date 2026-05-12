package com.example.didqr.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class QrUtil {

    public static String generateQrImage(Long documentId) {
        try {
            String verifyUrl = "http://192.168.200.185:8080/verify/" + documentId;

            String fileName = "document-" + documentId + ".png";
            Path path = Paths.get("qr/" + fileName);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    verifyUrl,
                    BarcodeFormat.QR_CODE,
                    250,
                    250
            );

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("QR 코드 생성 실패", e);
        }
    }
}