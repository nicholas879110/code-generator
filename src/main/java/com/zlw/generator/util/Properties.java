package com.zlw.generator.util;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class Properties {
    private String key;
    private String value;

    public Properties() {
    }

    public Properties(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
