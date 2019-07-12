package com.zlw.generator.db;

import com.zlw.generator.db.crud.DataBaseManager;
import com.zlw.generator.db.domain.*;
import com.zlw.generator.util.FieldUtil;
import com.zlw.generator.util.LoadProperties;
import com.zlw.generator.util.ObjectHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class OracleDatabaseInfo extends ADatabaseInfo  {

    public OracleDatabaseInfo(LoadProperties fieldmapping) {
        super(fieldmapping);
    }
    /**
     * 默认获取前20张表.
     * @return 表信息列表
     */
    @Override
    public List getTableList() {
        try {
            DataBaseManager hyberbin = new DataBaseManager(Constants.getOracleConnection());
            List mapToTableInfo = mapToTableInfo(hyberbin.getMapList("select TABLE_name as \"table\"  ,comments as \"COMMENT\"  from USER_tab_comments where INSTR(TABLE_NAME,'$')=0 and ROWNUM<20 "));
            return mapToTableInfo;
        } catch (SQLException ex) {
            log.error("getTableList error", ex);
        }
        return null;
    }

    /**
     * 按照表名称模糊搜索表.
     * @param search 表名称信息
     * @return 表信息列表
     */
    @Override
    public List<TableInfo> getTableList(String search) {
        try {
            DataBaseManager hyberbin = new DataBaseManager(Constants.getOracleConnection());
            search = search.trim();
            StringBuilder sql = new StringBuilder(" select TABLE_name as \"table\"  ,comments as \"COMMENT\"  from USER_tab_comments where INSTR(TABLE_NAME,'$')=0 ");
            if (search.contains(" ")) {
                sql.append(" and table_name in('").append(search.toUpperCase().replace(" ", "','")).append("')");
            } else {
                sql.append(" and table_name like ? ");
                hyberbin.addParmeter("%" + search.toUpperCase() + "%");
            }
            List mapToTableInfo = mapToTableInfo(hyberbin.getMapList(sql.toString()));
            return mapToTableInfo;
        } catch (SQLException ex) {
            log.error("getTableList error", ex);
        }
        return null;
    }

    /**
     * 按照表名称搜索表.
     * @param tables 表名称信息
     * @return 表信息列表
     */
    @Override
    public List<TableInfo> getTableList(String[] tables) {
        try {
            if (ObjectHelper.isEmpty(tables)) {
                return new ArrayList();
            }
            DataBaseManager hyberbin = new DataBaseManager(Constants.getOracleConnection());
            StringBuilder sql = new StringBuilder(" and table_name in('none'");
            for (String table : tables) {
                sql.append(",'").append(table.toUpperCase()).append("'");
            }
            sql.append(")");
            List mapToTableInfo = mapToTableInfo(hyberbin.getMapList("select TABLE_name as \"table\"  ,comments as \"COMMENT\"  from USER_tab_comments where INSTR(TABLE_NAME,'$')=0 " + sql.toString()));
            return mapToTableInfo;
        } catch (SQLException ex) {
            log.error("getTableList error", ex);
        }
        return null;
    }

    /**
     * 根据表的基本信息获得表的完整信息.
     * @param tableBean 带有表基本信息的tableBean
     */
    @Override
    public void getTableInfo(TableBean tableBean) {
        try {
            DataBaseManager hyberbin = new DataBaseManager(Constants.getOracleConnection());
            List<ColumnModel> columnList = new ArrayList();
            List<Map> mapList = hyberbin.getMapList("select \"columns\".data_length as \"length\" ,\"columns\".COLUMN_ID AS \"column_key\" ,\"columns\".COLUMN_NAME AS \"column\" , " +
                    "\"comments\".COMMENTS AS \"COMMENT\",\"columns\".DATA_TYPE AS \"data_type\" " +
                    " from user_tab_columns \"columns\", " +
                    " user_col_comments \"comments\" " +
                    " where \"columns\".Table_Name= \"comments\".Table_Name AND \"columns\".COLUMN_NAME =\"comments\".COLUMN_NAME  and \"columns\".table_name='" + tableBean.getTableName() + "' order by \"columns\".COLUMN_ID ");
            for (Map map : mapList) {
                ColumnModel columnModel = new ColumnModel();
                String column= ((String) map.get("column")).toLowerCase();
                String comment=(String) map.get("comment");
                columnModel.setColumnName(column);
                columnModel.setFieldComment(comment==null?column:comment);
                columnModel.setFieldName(FieldUtil.INSTANCE.columnToField(columnModel.getColumnName().toLowerCase(), false));
                String data_type = (String) map.get("data_type");
                columnModel.setDataType(data_type);
                String property = getJavaType((String) map.get("data_type"),tableBean.getTableName(),columnModel.getColumnName());
                if (ObjectHelper.isNullOrEmptyString(property)) {
                    log.error(data_type + "找不到对应的java类型！");
                }
                columnModel.setFieldType(property);
                Object length = map.get("length");
                Object column_key = map.get("column_key");
                columnModel.setIsAutoIncrement(false);
                if (ObjectHelper.isNotEmpty(length)) {
                    columnModel.setLength(Integer.parseInt(length.toString()));
                }
                if ("1".equals(column_key.toString())) {
                    columnModel.setIsPri(true);
                }
                columnList.add(columnModel);
            }
            tableBean.setColumnList(columnList);

        } catch (SQLException ex) {
            log.error("getTableInfo error", ex);
        }
    }

    @Override
    public List<EnumBean> getEnumBeans(String tableName, String keyColumn, String englishColumn, String chinseColumn, String pingyingColumn) {
        return null;
    }

    @Override
    public List<Map> getCodeList(String name) {
        return null;
    }

    @Override
    public List<String> getAllCodeList() {
        return null;
    }

    @Override
    public List<CodeEnumBean> getCodeEnumBeans(String sjx) {
        return null;
    }
}
