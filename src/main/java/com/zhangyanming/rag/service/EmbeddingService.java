package com.zhangyanming.rag.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * 文本向量化服务
 * 将文本转为向量，用于RAG的文档检索
 */
@Service
public class EmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.api-key}") private String apiKey;
    @Value("${ai.base-url}") private String baseUrl;

    /**
     * 将文本转为向量（调用通义千问Embedding API）
     */
    public List<Double> embed(String text) {
        String url = baseUrl + "/embeddings";

        Map<String, Object> body = Map.of(
            "model", "text-embedding-v3",
            "input", text
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var resp = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), Map.class);
        var data = (List<Map>) resp.getBody().get("data");
        var embedding = (List<Double>) data.get(0).get("embedding");

        return embedding;
    }
}
