package com.zhangyanming.rag.controller;

import com.zhangyanming.rag.common.R;
import com.zhangyanming.rag.entity.Document;
import com.zhangyanming.rag.service.AIService;
import com.zhangyanming.rag.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

/**
 * RAG智能知识库 — REST接口
 * 统一使用 R<T> 响应格式，对标企业级API规范
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final AIService aiService;
    private final DocumentService docService;

    /** RAG增强问答 */
    @PostMapping("/chat/rag")
    public R<Map<String, Object>> chatRAG(@RequestBody Map<String, String> req) {
        String question = req.get("question");
        List<String> docs = docService.searchRelevant(question);
        String reply = aiService.chatWithDocs(question, docs);
        return R.ok(Map.of("reply", reply, "sources", docs.size()));
    }

    /** 上传文档 */
    @PostMapping("/documents")
    public R<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) {
        Document doc = docService.upload(file);
        return R.ok(Map.of("docId", doc.getId(), "fileName", doc.getFileName()));
    }
}
