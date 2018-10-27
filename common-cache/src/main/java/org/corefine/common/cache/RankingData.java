package org.corefine.common.cache;

/**
 * 排行榜数据
 * Created by Chris on 2016/12/26.
 */
public class RankingData<T> {
    private T element;
    private double score;

    public RankingData(T element, double score) {
        this.element = element;
        this.score = score;
    }

    public Object getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
