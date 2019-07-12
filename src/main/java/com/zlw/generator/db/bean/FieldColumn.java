package com.zlw.generator.db.bean;

import java.lang.reflect.Field;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class FieldColumn {
    private Field field;
    private String column;
    private int length;
    private boolean hasGetterAndSetter;
    private boolean ignore;
    private int sqltype;

    public FieldColumn(Field field, String column, int length, boolean hasGetterAndSetter, boolean ignore) {
        this.field = field;
        this.column = column;
        this.length = length;
        this.hasGetterAndSetter = hasGetterAndSetter;
        this.ignore = ignore;
    }

    public FieldColumn() {
    }

    public Field getField() {
        return this.field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isHasGetterAndSetter() {
        return this.hasGetterAndSetter;
    }

    public void setHasGetterAndSetter(boolean hasGetterAndSetter) {
        this.hasGetterAndSetter = hasGetterAndSetter;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isIgnore() {
        return this.ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public int getSqltype() {
        return this.sqltype;
    }

    public void setSqltype(int sqltype) {
        this.sqltype = sqltype;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(FieldColumn.class) && this.column.equals(((FieldColumn)obj).getColumn());
    }

    @Override
    public int hashCode() {
        byte hash = 5;
        int hash1 = 67 * hash + (this.column != null?this.column.hashCode():0);
        return hash1;
    }

    public FieldColumn cloneMe(Field field) {
        return new FieldColumn(field, this.column, this.length, this.hasGetterAndSetter, this.ignore);
    }
}
