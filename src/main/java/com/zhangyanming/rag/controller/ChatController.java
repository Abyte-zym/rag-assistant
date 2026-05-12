package com.zhangyanming.rag.controller;

import com.zhangyanming.rag.entity.Document;
import com.zhangyanming.rag.service.AIService;
import com.zhangyanming.rag.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired private AIService aiService;
    @Autowired private DocumentService docService;

    /** RAG增强问答 */
    @PostMapping("/chat/rag")
    public Map<String, Object> chatRAG(@RequestBody Map<String, String> req) {
        String question = req.get("question");
        List<String> docs = docService.searchRelevant(question);
        String reply = aiService.chatWithDocs(question, docs);
        return Map.of("reply", reply, "sources", docs.size());
    }

    /** 上传文档 */
    @PostMapping("/documents")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        try {
            Document doc = docService.upload(file);
            return Map.of("success", true, "docId", doc.getId(),
                         "fileName", doc.getFileName());
        } catch (Exception e) {
            return Map.of("success", false, "error", e.getMessage());
        }
    }
}
