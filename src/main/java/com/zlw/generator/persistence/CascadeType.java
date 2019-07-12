package com.zlw.generator.persistence;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/27
 *
 */
public enum CascadeType {
    ALL,
    PERSIST,
    MERGE,
    REMOVE,
    REFRESH,
    DETACH;

    private CascadeType() {
    }
}
