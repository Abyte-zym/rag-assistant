package com.zhangyanming.rag.common;

import org.slf4j.MDC;
import jakarta.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * 请求链路追踪过滤器 — 每次请求生成唯一traceId
 * 对标RuoYi-Cloud HeaderInterceptor，配合Logback输出日志追踪
 */
public class TraceFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        try {
            MDC.put("traceId", UUID.randomUUID().toString().replace("-", "").substring(0, 12));
            chain.doFilter(req, resp);
        } finally {
            MDC.remove("traceId");
        }
    }
}
