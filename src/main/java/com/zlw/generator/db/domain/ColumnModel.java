/*
 * Copyright 2015 www.hyberbin.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Email:hyberbin@qq.com
 */
package com.zlw.generator.db.domain;

/**
 * ColumnModel说明.
 * @author Hyberbin
 * @date 2013-6-28 17:27:13
 */
public class ColumnModel extends FieldModel {

    /** 字段名 */
    private String columnName;
    /** 数据类型 */
    private String dataType;
    /** 长度 */
    private int length;
    /** 是否是主键 */
    private boolean isPri;
    /** 是否是索引 */
    private boolean isIndex;
    /** 是否是自增长类型 */
    private boolean isAutoIncrement;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean getIsPri() {
        return isPri;
    }

    public void setIsPri(boolean isPri) {
        this.isPri = isPri;
    }

    public boolean getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(boolean isIndex) {
        this.isIndex = isIndex;
    }

    public boolean getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().isAssignableFrom(this.getClass())||columnName==null) {
            return false;
        }
        if (getFieldName().equals(((FieldModel) obj).getFieldName())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.columnName != null ? this.columnName.hashCode() : 0);
        return hash;
    }
    
     public boolean isKey(){
        return isPri;
    }
     
     public boolean isNullable(){
       return !isPri;
   }
}
