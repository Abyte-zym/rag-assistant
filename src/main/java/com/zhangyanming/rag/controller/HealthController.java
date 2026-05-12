package com.zhangyanming.rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "running",
            "service", "RAG智能知识库系统",
            "version", "1.0.0"
        );
    }
}
