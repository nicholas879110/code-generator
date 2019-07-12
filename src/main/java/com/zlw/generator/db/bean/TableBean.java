package com.zlw.generator.db.bean;

import com.zlw.generator.db.util.IgnoreCaseMap;
import com.zlw.generator.util.ObjectHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/27
 *
 */
public class TableBean {

    private String tableName;
    private List<FieldColumn> columns;
    private Map<String, FieldColumn> columnMap;
    private String primaryKey = "id";

    public TableBean() {
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<FieldColumn> getColumns() {
        return this.columns;
    }

    public String getPrimaryKey() {
        return this.primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setColumns(List<FieldColumn> columns) {
        this.columns = columns;
        this.columnMap = new IgnoreCaseMap();
        if(ObjectHelper.isNotEmpty(columns)) {
            Iterator var2 = columns.iterator();

            while(var2.hasNext()) {
                FieldColumn column = (FieldColumn)var2.next();
                this.columnMap.put(column.getColumn(), column);
            }
        }

    }

    public Map<String, FieldColumn> getColumnMap() {
        return this.columnMap;
    }
}
