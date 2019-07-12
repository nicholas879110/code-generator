package com.zlw.generator.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class GetSql {

    private final List fields = new ArrayList();
    private final List values = new ArrayList();
    private final List conditions = new ArrayList();

    public GetSql() {
    }

    public void add(String field, String value) {
        this.fields.add(field);
        this.values.add(value);
        this.conditions.add("");
    }

    public void add(String field, String value, String condition) {
        this.fields.add(field);
        this.values.add(value);
        if(condition == null) {
            this.conditions.add("");
        } else if(condition.equals("where")) {
            this.conditions.add("where");
        } else if(condition.equals("null")) {
            this.conditions.add("null");
        } else {
            this.conditions.add("");
        }

    }

    public String getInsert(String from) {
        StringBuilder insertString = new StringBuilder();
        StringBuffer fieldsString = new StringBuffer();
        StringBuffer valuesString = new StringBuffer();

        for(int i = 0; i < this.fields.size(); ++i) {
            if(this.values.get(i) != null && !this.values.get(i).equals("")) {
                fieldsString.append(this.fields.get(i)).append(",");
                valuesString.append(this.values.get(i)).append(",");
            }
        }

        if(fieldsString.length() > 0) {
            fieldsString.deleteCharAt(fieldsString.length() - 1).insert(0, "(").append(")");
        }

        if(valuesString.length() > 0) {
            valuesString.deleteCharAt(valuesString.length() - 1).insert(0, "(").append(")");
        }

        insertString.append("insert into ").append(from).append(fieldsString).append(" values").append(valuesString);
        return insertString.toString();
    }

    public String getUpdate(String from) {
        StringBuilder updateString = new StringBuilder();
        StringBuffer valuesString = new StringBuffer();
        StringBuffer conditionsString = new StringBuffer();

        for(int i = 0; i < this.fields.size(); ++i) {
            String field = (String)this.fields.get(i);
            String value = (String)this.values.get(i);
            if("".equals(value)) {
                value = null;
            }

            if(this.conditions.get(i).equals("")) {
                valuesString.append(field).append("=").append(value).append(",");
            } else if(this.conditions.get(i).equals("null")) {
                if(value != null) {
                    valuesString.append(field).append("=").append(value).append(",");
                }
            } else {
                conditionsString.append(" and ").append(field).append(" = ").append(value);
            }
        }

        if(valuesString.length() > 0) {
            valuesString.deleteCharAt(valuesString.length() - 1);
        }

        if(conditionsString.length() > 0) {
            conditionsString.delete(0, 4);
        }

        updateString.append("update ").append(from).append(" set ").append(valuesString).append(" where").append(conditionsString);
        return updateString.toString();
    }

    public String getQuery(String selectString) {
        return this.getQuery(selectString, "");
    }

    public String getQuery(String selectString, String orderby) {
        StringBuilder queryString = new StringBuilder();
        StringBuffer conditionsString = new StringBuffer();

        for(int i = 0; i < this.fields.size(); ++i) {
            String field = (String)this.fields.get(i);
            String value = (String)this.values.get(i);
            if(value != null && !value.equals("")) {
                conditionsString.append(" and ").append(field).append(" = ").append(value);
            }
        }

        if(selectString.indexOf("where") == -1) {
            conditionsString.delete(0, 5).insert(0, "where");
        }

        queryString.append(selectString).append(" ").append(conditionsString).append("").append(orderby);
        return queryString.toString();
    }

    public String sqlserverPageSql(String tableName, String strWhere, String key, String strOrder, int pageSize, int top) {
        String strTmp;
        if(strOrder.contains("desc")) {
            strTmp = "<(select min";
            strOrder = " order by " + key + " desc";
        } else {
            strTmp = ">(select max";
            strOrder = " order by " + key + " asc";
        }

        String strSQL;
        if(top == 0) {
            if(!strWhere.equals("")) {
                strSQL = "select top " + pageSize + " * from " + tableName + " " + strWhere + " " + strOrder;
            } else {
                strSQL = "select top " + pageSize + " * from " + tableName + " " + strOrder;
            }
        } else {
            strSQL = "select top " + pageSize + " * from " + tableName + " where " + key + " " + strTmp + "(" + key + ") from (select top " + top + " " + key + " from " + tableName + strOrder + ") as tblTmp)" + strOrder;
            if(!strWhere.equals("")) {
                strSQL = "select top " + pageSize + " * from " + tableName + " where " + key + " " + strTmp + "(" + key + ") from (select top " + top + " " + key + " from " + tableName + " " + strWhere + " " + strOrder + ") as tblTmp) and " + strWhere.replaceFirst("where", "") + " " + strOrder;
            }
        }

        return strSQL;
    }
}
