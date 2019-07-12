package com.zlw.generator.db.bean;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class ParmeterPair {

    private final Object parmeter;
    private final FieldColumn fieldColumn;

    public ParmeterPair(Object parmeter, FieldColumn fieldColumn) {
        this.parmeter = parmeter;
        this.fieldColumn = fieldColumn;
    }

    public Object getParmeter() {
        return this.parmeter;
    }

    public FieldColumn getFieldColumn() {
        return this.fieldColumn;
    }
}
