package com.example.didqr.controller;

import com.example.didqr.entity.Document;
import com.example.didqr.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam String content
    ) {
        Document document = documentService.saveDocument(title, issuerDid, content);

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
}