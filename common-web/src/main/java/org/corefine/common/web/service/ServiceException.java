package org.corefine.common.web.service;

import org.corefine.common.web.constant.CodeConstant;

/**
 * 服务异常
 */
public class ServiceException extends RuntimeException {
    private final int code;
    private final String message;

    public ServiceException() {
        this(CodeConstant.SERVICE_ERROR, "服务异常");
    }

    public ServiceException(String message) {
        this(CodeConstant.SERVICE_ERROR, message);
    }

    public ServiceException(int code, String message) {
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
