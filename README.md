# RAG智能知识库系统

基于RAG架构的AI智能问答系统，上传文档后自动分块存储，提问时检索相关内容并调用大模型生成精准回答。

## 技术栈
Spring Boot 3 + Spring AI + Redis + MySQL + Docker

## 功能
- 文档上传与自动分块
- 关键词检索相关文档片段
- 接入大模型API生成回答（RAG模式）
- Docker Compose一键部署

## 快速启动
```bash
# 1. 配置API Key
export QWEN_API_KEY=你的通义千问API Key

# 2. 启动
docker-compose up -d

# 3. 访问
http://localhost:8080
```

## API
- `POST /api/chat` - 普通对话
- `POST /api/chat/rag` - RAG增强问答
- `POST /api/documents` - 上传文档

## 项目结构
```
src/
├── main/java/com/example/rag/
│   ├── RagApplication.java
│   ├── controller/ChatController.java
│   ├── service/AIService.java
│   ├── service/DocumentService.java
│   └── entity/Document.java
└── main/resources/
    └── application.yml
```

## 开发计划
- [x] 项目骨架搭建
- [x] AI对话接口
- [ ] 文档分块与向量化存储
- [ ] 前端交互页面
- [ ] Agent多步推理升级
