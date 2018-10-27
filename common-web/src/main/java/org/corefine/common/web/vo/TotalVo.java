package org.corefine.common.web.vo;

import java.util.List;

public class TotalVo<T> extends StatusVo {
    private Long total;
    private List<T> data;

    public TotalVo() {}

    public TotalVo(List<T> data, Long total) {
        this.data = data;
        this.total = total;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
