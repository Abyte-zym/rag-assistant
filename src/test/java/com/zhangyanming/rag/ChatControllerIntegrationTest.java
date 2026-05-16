package com.zhangyanming.rag;

import com.zhangyanming.rag.common.R;
import com.zhangyanming.rag.controller.ChatController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ChatController 集成测试
 * 验证 RAG 接口的端到端流程（使用 test profile）
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ChatControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        // Spring容器加载验证
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void chatRAGShouldReturnReply() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("question", "什么是Spring Boot?"), headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/chat/rag", request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("reply");
    }
}
