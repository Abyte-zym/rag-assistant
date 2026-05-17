package com.zhangyanming.rag.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * AI大模型服务
 * 封装REST调用，负责Prompt组装、API请求、响应解析
 * 企业级增强：结构化日志记录每次调用的耗时和结果
 */
@Slf4j
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
        long start = System.currentTimeMillis();
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

        try {
            var resp = restTemplate.postForEntity(
                    url, new HttpEntity<>(body, headers), Map.class);
            var choices = (List<Map>) resp.getBody().get("choices");
            var msg = (Map) choices.get(0).get("message");
            String reply = (String) msg.get("content");

            log.info("AI调用成功 | {}ms | 文档{}条 | 问题{}字",
                    System.currentTimeMillis() - start, docs.size(), question.length());
            return reply;
        } catch (Exception e) {
            log.error("AI调用失败 | {}ms | error={}",
                    System.currentTimeMillis() - start, e.getMessage());
            throw e;
        }
    }
}
