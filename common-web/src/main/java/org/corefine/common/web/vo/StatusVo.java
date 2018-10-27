package org.corefine.common.web.vo;

import org.corefine.common.web.constant.CodeConstant;

public class StatusVo {
    private Integer code;
    public StatusVo() {
        this(CodeConstant.SUCCESS);
    }
    public StatusVo(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
