package com.zhangyanming.rag.common;

/**
 * 业务异常 — 统一异常体系
 */
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(String message) { super(message); this.code = 500; }
    public BusinessException(int code, String message) { super(message); this.code = code; }
    public int getCode() { return code; }
}
