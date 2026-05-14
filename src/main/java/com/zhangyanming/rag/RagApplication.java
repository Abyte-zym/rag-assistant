package com.zhangyanming.rag;

import com.zhangyanming.rag.common.TraceFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RagApplication {
    public static void main(String[] args) {
        SpringApplication.run(RagApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /** 注册请求链路追踪过滤器 */
    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilter() {
        FilterRegistrationBean<TraceFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new TraceFilter());
        reg.addUrlPatterns("/*");
        reg.setOrder(1);
        return reg;
    }
}
