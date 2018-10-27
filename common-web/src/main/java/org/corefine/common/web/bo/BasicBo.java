package org.corefine.common.web.bo;

import javax.validation.constraints.NotNull;

public class BasicBo {
    @NotNull(message = "请求类型不能为空")
    private String request;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRequest() {
        return request;
    }

    public BasicBo setRequest(String request) {
        this.request = request;
        return this;
    }
}
