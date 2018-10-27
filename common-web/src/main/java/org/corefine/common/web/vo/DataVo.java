package org.corefine.common.web.vo;

public class DataVo<T> extends StatusVo {
    private T data;

    public DataVo() {}

    public DataVo(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
