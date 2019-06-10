package org.corefine.common.web.bo;

public class AuthedBo implements Authed {
    private Long userId;

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;

    }

    public Long getUserId() {
        return userId;
    }
}
