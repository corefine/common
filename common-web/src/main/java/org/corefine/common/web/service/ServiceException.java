package org.corefine.common.web.service;

import org.corefine.common.web.constant.CodeConstant;

/**
 * 服务异常
 */
public class ServiceException extends RuntimeException {
    private final int code;
    private final String message;

    public ServiceException() {
        this(CodeConstant.SERVICE_ERROR, "服务异常", null);
    }

    public ServiceException(String message) {
        this(CodeConstant.SERVICE_ERROR, message, null);
    }

    public ServiceException(String message, Throwable throwable) {
        this(CodeConstant.SERVICE_ERROR, message, throwable);
    }

    public ServiceException(int code, String message) {
        this(code, message, null);
    }

    public ServiceException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
