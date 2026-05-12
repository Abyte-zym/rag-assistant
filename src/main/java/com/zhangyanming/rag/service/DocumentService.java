package com.zhangyanming.rag.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangyanming.rag.entity.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Service
public class DocumentService {

    @Autowired private DocumentRepository docRepo;
    @Autowired private AIService aiService;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * 上传文档：提取文本→分块→存储
     */
    public Document upload(MultipartFile file) throws Exception {
        String text = new String(file.getBytes());
        List<String> chunks = splitChunks(text, 500, 50);

        Document doc = new Document();
        doc.setFileName(file.getOriginalFilename());
        doc.setContent(text);
        doc.setChunks(mapper.writeValueAsString(chunks));

        return docRepo.save(doc);
    }

    /**
     * 检索相关文档片段
     */
    public List<String> searchRelevant(String query) {
        List<Document> allDocs = docRepo.findAll();
        List<String> results = new ArrayList<>();

        for (Document doc : allDocs) {
            try {
                List<String> chunks = mapper.readValue(doc.getChunks(), new TypeReference<>(){});
                for (String chunk : chunks) {
                    if (containsKeyword(chunk, query) && results.size() < 5) {
                        results.add(chunk);
                    }
                }
            } catch (Exception ignored) {}
        }
        return results;
    }

    private List<String> splitChunks(String text, int size, int overlap) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < text.length(); i += (size - overlap)) {
            int end = Math.min(i + size, text.length());
            chunks.add(text.substring(i, end));
        }
        return chunks;
    }

    private boolean containsKeyword(String text, String query) {
        for (String word : query.split("\\s+")) {
            if (text.contains(word)) return true;
        }
        return text.contains(query);
    }
}
