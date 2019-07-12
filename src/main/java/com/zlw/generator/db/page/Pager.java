package com.zlw.generator.db.page;

import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class Pager<T> {

    private Integer current;
    private Integer size;
    private Integer items;
    private Integer nextPage;
    private Integer upPage;
    private Integer totalPage;
    private Integer first;
    private List<T> data;

    public Pager(Integer size) {
        this();
        this.size = size;
    }

    public Pager() {
        this.current = Integer.valueOf(1);
        this.size = Integer.valueOf(5);
        this.items = Integer.valueOf(0);
        this.nextPage = Integer.valueOf(1);
        this.upPage = Integer.valueOf(1);
        this.totalPage = Integer.valueOf(1);
        this.first = Integer.valueOf(1);
    }

    public Integer getCurrent() {
        if(this.current.intValue() < 1) {
            this.current = Integer.valueOf(1);
        }

        return this.current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getItems() {
        return this.items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }

    public Integer getTop() {
        return Integer.valueOf(this.current.intValue() - 1 >= 0?(this.current.intValue() - 1) * this.size.intValue():0);
    }

    public Integer getNextPage() {
        if(this.current.intValue() >= this.getTotalPage().intValue()) {
            this.nextPage = this.current;
        } else {
            this.nextPage = Integer.valueOf(this.current.intValue() + 1);
        }

        return this.nextPage;
    }

    public Integer getUpPage() {
        if(this.current.intValue() > 1) {
            this.upPage = Integer.valueOf(this.current.intValue() - 1);
        } else {
            this.upPage = Integer.valueOf(1);
        }

        return this.upPage;
    }

    public Integer getTotalPage() {
        this.totalPage = Integer.valueOf(this.items.intValue() % this.size.intValue() == 0?this.items.intValue() / this.size.intValue():this.items.intValue() / this.size.intValue() + 1);
        return this.totalPage;
    }

    public Integer getFirst() {
        this.first = Integer.valueOf(1);
        return this.first;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
