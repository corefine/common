package org.corefine.common.web.vo;

import org.corefine.common.web.constant.CodeConstant;

public class MessageVo extends StatusVo {
    private String message;

    public MessageVo(String message) {
        this(CodeConstant.SUCCESS, message);
    }

    public MessageVo(Integer code, String message) {
        super(code);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public StatusVo setMessage(String message) {
        this.message = message;
        return this;
    }
}
