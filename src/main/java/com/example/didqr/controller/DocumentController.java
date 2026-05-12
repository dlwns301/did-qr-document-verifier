package com.example.didqr.controller;

import com.example.didqr.entity.Document;
import com.example.didqr.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/")
    public String home() {
        return "redirect:/documents/new";
    }

    @GetMapping("/documents/new")
    public String documentForm() {
        return "document-form";
    }

    @PostMapping("/documents")
    public String createDocument(
            @RequestParam String title,
            @RequestParam String issuerDid,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) MultipartFile file
    ) {
        Document document = documentService.saveDocument(title, issuerDid, content, file);

        return "redirect:/documents/" + document.getId();
    }

    @GetMapping("/documents/{id}")
    public String documentDetail(@PathVariable Long id, Model model) {
        Document document = documentService.findDocument(id);

        model.addAttribute("document", document);

        return "document-detail";
    }

    @GetMapping("/verify/{id}")
    public String verifyForm(@PathVariable Long id, Model model) {
        Document document = documentService.findDocument(id);

        model.addAttribute("document", document);

        return "verify";
    }

    @PostMapping("/verify/{id}")
    public String verifyDocument(
            @PathVariable Long id,
            @RequestParam String content,
            Model model
    ) {
        Document document = documentService.findDocument(id);
        boolean result = documentService.verifyDocument(id, content);
        String inputHash = documentService.getInputHash(content);

        model.addAttribute("document", document);
        model.addAttribute("result", result);
        model.addAttribute("inputHash", inputHash);

        return "verify";
    }

    @PostMapping("/verify/{id}/file")
    public String verifyDocumentFile(
            @PathVariable Long id,
            @RequestParam MultipartFile file,
            Model model
    ) {
        Document document = documentService.findDocument(id);
        boolean result = documentService.verifyDocumentFile(id, file);
        String inputHash = documentService.getFileHash(file);

        model.addAttribute("document", document);
        model.addAttribute("result", result);
        model.addAttribute("inputHash", inputHash);
        model.addAttribute("verifyType", "file");

        return "verify";
    }
}