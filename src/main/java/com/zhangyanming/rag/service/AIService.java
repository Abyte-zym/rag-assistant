package com.zhangyanming.rag.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.api-key}") private String apiKey;
    @Value("${ai.base-url}") private String baseUrl;
    @Value("${ai.model}") private String model;

    /**
     * RAG问答：基于文档检索结果+大模型生成回答
     * 先检索再增强生成，提升回答准确率
     */
    public String chatWithDocs(String question, List<String> docs) {
        String context = String.join("\n---\n", docs);
        String prompt = String.format(
            "基于以下文档回答问题。如果文档中没有相关信息，请明确说明。\n\n文档：\n%s\n\n问题：%s",
            context, question
        );

        String url = baseUrl + "/chat/completions";
        Map<String, Object> body = Map.of(
            "model", model,
            "messages", List.of(Map.of("role", "user", "content", prompt)),
            "temperature", 0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var resp = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), Map.class);
        var choices = (List<Map>) resp.getBody().get("choices");
        var msg = (Map) choices.get(0).get("message");
        return (String) msg.get("content");
    }
}
