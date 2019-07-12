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


import com.zlw.generator.util.FieldUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TableBean说明.
 *
 * @author Hyberbin
 * @date 2013-6-7 10:07:56
 */
public class TableBean {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 生成的PO类名
     */
    private String entityName;
    /**
     * 字段列表
     */
    private List<ColumnModel> columnList;
    /**
     * 表的注释
     */
    private String tableComment;

    private PrimaryKey primaryKey;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEntityName() {
        return entityName == null ? FieldUtil.INSTANCE.columnToField(tableName, true) : entityName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public void setColumnList(List columnList) {
        this.columnList = columnList;
    }

    public List<ColumnModel> getColumnList() {
        return columnList;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }


    public PrimaryKey getPrimaryKey() {
        PrimaryKey primaryKey = null;
        for (ColumnModel columnModel : columnList) {
            if (columnModel.getIsPri()) {
                primaryKey = new PrimaryKey();
                primaryKey.setPriKey(columnModel.getColumnName());
                primaryKey.setPriField(columnModel.getFieldName());
                primaryKey.setPriDataType(columnModel.getJavaType());
                primaryKey.setUpperField(columnModel.getFieldName().substring(0, 1).toUpperCase() + columnModel.getFieldName().substring(1));
            }
        }
        return primaryKey;
    }

    /*************************************************************/
    private String createTimeColumnName = "createTime";

    private String updateTimeColumnName = "modifyTime";

    public String getTablesNameToClassName(String tableName) {
        String[] split = tableName.split("_");
        if (split.length > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                String tempTableName = split[i].substring(0, 1).toUpperCase()
                        + split[i].substring(1, split[i].length());
                sb.append(tempTableName);
            }

            return sb.toString();
        }
        String tempTables = split[0].substring(0, 1).toUpperCase() + split[0].substring(1, split[0].length());
        return tempTables;
    }

    private String method;

    private String argv;

    public String getBeanFeilds() throws SQLException {
        List<ColumnModel> dataList = this.getColumnList();
        StringBuffer str = new StringBuffer();
        StringBuffer getset = new StringBuffer();
        for (ColumnModel d : dataList) {
            String name = d.getFieldName();
            String type = d.getJavaType();
            String comment = d.getFieldComment();

            //尝试从注释中匹配枚举类型，如#AccountTypeEnum
            Pattern pattern4Enum = Pattern.compile("#(\\w+Enum)");
            Matcher matcher = pattern4Enum.matcher(comment == null ? "" : comment);
            if (matcher.find()) {
                type = matcher.group(1);
            } else {
                int index = type.lastIndexOf(".");
                if (index > -1) {
                    type = type.substring(index + 1);// 使用短类型
                }
            }

            String maxChar = name.substring(0, 1).toUpperCase();
            str.append("\r\t").append("private ").append(type + " ").append(name).append(";//   ").append(comment);
            String method = maxChar + name.substring(1, name.length());
            getset.append("\r\t").append("public ").append(type + " ").append("get" + method + "() {\r\t");
            getset.append("    return this.").append(name).append(";\r\t}");
            getset.append("\r");
            getset.append("\r\t").append("public void ").append("set" + method + "(" + type + " " + name + ") {\r\t");
            getset.append("    this." + name + " = ").append(name).append(";\r\t}");
            getset.append("\r");
        }
        this.argv = str.append("\r").toString();
        this.method = getset.toString();
        return this.argv + this.method;
    }


    /**
     * @param
     * @return
     * @throws Exception
     */
    public Map<String, Object> getAutoCreateSql() throws Exception {
        Map sqlMap = new HashMap();
        List<ColumnModel> columnDatas = this.getColumnList();
        String columns = getColumnSplit(columnDatas);
        String fields = getFieldSplit(columnDatas);
        String[] columnList = getColumnList(columns);
        String columnFields = getColumnFields(columns);
        String insert = "insert into\n\t\t" + tableName + "(" + columns.replaceAll("\\|", ",") + ")\n\t\tvalues(#{"
                + fields.replaceAll("\\|", "},#{") + "})";
        String update = getUpdateSql(tableName, columnList);
        String updateSelective = getUpdateSelectiveSql(tableName, columnDatas);
        String selectById = getSelectByIdSql(tableName, columnList);
        String delete = getDeleteSql(tableName, columnList);
        sqlMap.put("columnList", columnList);
        sqlMap.put("columnFields", columnFields);
        String insertReplaced = insert.replace("#{" + createTimeColumnName + "}", "now()").replace("#{" + createTimeColumnName + "}", "now()");
        insertReplaced = insertReplaced.replace("#{" + updateTimeColumnName + "}", "now()").replace("#{" + updateTimeColumnName + "}", "now()");
        sqlMap.put("insert", insertReplaced);
        sqlMap.put("update", update.replace("#{" + updateTimeColumnName + "}", "now()").replace("#{" + updateTimeColumnName + "}", "now()"));
        sqlMap.put("delete", delete);
        sqlMap.put("updateSelective", updateSelective);
        sqlMap.put("selectById", selectById);
        return sqlMap;
    }


    public String[] getColumnList(String columns) throws SQLException {
        String[] columnList = columns.split("[|]");
        return columnList;
    }

    public String getDeleteSql(String tableName, String[] columnsList) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from ").append(tableName).append(" where\n\t\t");
        sb.append(columnsList[0]).append("=#{").append(toCamelCase(columnsList[0])).append("}");
        return sb.toString();
    }


    public String getColumnSplit(List<ColumnModel> columnList) throws SQLException {
        StringBuffer commonColumns = new StringBuffer();
        for (ColumnModel data : columnList) {
            commonColumns.append(data.getColumnName() + "|");
        }
        return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
    }

    public String getFieldSplit(List<ColumnModel> columnList) throws SQLException {
        StringBuffer commonColumns = new StringBuffer();
        for (ColumnModel data : columnList) {
            commonColumns.append(data.getFieldName() + "|");
        }
        return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
    }

    public String getColumnFields(String columns) throws SQLException {
        String fields = columns;
        if ((fields != null) && (!"".equals(fields))) {
            fields = fields.replaceAll("[|]", ",");
        }
        return fields;
    }

    public String getUpdateSql(String tableName, String[] columnsList) throws SQLException {
        StringBuffer sb = new StringBuffer();

        for (int i = 1; i < columnsList.length; i++) {
            String column = columnsList[i];
            if (!createTimeColumnName.equalsIgnoreCase(column)) {
                if (updateTimeColumnName.equalsIgnoreCase(column.toUpperCase())) {
                    sb.append(column + "=now()");
                } else {
                    sb.append(column + "=#{" + toCamelCase(column) + "}");
                }
                if (i + 1 < columnsList.length) {
                    sb.append(",");
                }
            }
        }
        String update = "update " + tableName + " set\n\t\t" + sb.toString() + "\n\t\twhere " + columnsList[0] + "=#{"
                + toCamelCase(columnsList[0]) + "}";
        return update;
    }

    public String getUpdateSelectiveSql(String tableName, List<ColumnModel> columnList) throws SQLException {
        ColumnModel cd = (ColumnModel) columnList.get(0);
        String update = "update " + tableName + " set\n\t\t<include refid=\"selectiveSetClause\" />\n\t\twhere "
                + cd.getColumnName() + "=#{" + cd.getFieldName() + "}";
        return update;
    }

    public String getSelectByIdSql(String tableName, String[] columnsList) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("select\n\t\t<include refid=\"baseColumnList\" />\n");
        sb.append("\t\tfrom ").append(tableName).append(" where ");
        sb.append(columnsList[0]).append("=#{").append(toCamelCase(columnsList[0])).append("}");
        return sb.toString();
    }

    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

}
