package org.corefine.common.web.bo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PageBo<T> {
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最少1")
    @Max(value = 1000, message = "每页条数最多1000")
    private Integer pageSize = 10;
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer page;
    private T s;
    private Long userId;

    public Integer getStart() {
        return (this.page - 1) * this.pageSize;
    }

    public Integer getLength() {
        return this.pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public T getS() {
        return s;
    }

    public void setS(T s) {
        this.s = s;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
