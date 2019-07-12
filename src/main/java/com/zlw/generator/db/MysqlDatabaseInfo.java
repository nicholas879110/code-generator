package com.zlw.generator.db;

import com.zlw.generator.db.adapter.MysqlAdapter;
import com.zlw.generator.db.config.ConfigCenter;
import com.zlw.generator.db.crud.DataBaseManager;
import com.zlw.generator.db.domain.*;
import com.zlw.generator.db.transaction.IDbManager;
import com.zlw.generator.db.util.SqliteUtil;
import com.zlw.generator.util.FieldUtil;
import com.zlw.generator.util.LoadProperties;
import com.zlw.generator.util.ObjectHelper;
import com.zlw.generator.util.YouDaoTranslate;
import org.apache.commons.lang3.StringUtils;

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
public class MysqlDatabaseInfo extends ADatabaseInfo {

    public MysqlDatabaseInfo(LoadProperties fieldmapping) {
        super(fieldmapping);
        ConfigCenter.INSTANCE.setAdapter(MysqlAdapter.class);
    }

    /**
     * 默认获取前20张表.
     *
     * @return 表信息列表
     */
    @Override
    public List getTableList() {
        try {
            DataBaseManager hyberbin = new DataBaseManager(Constants.getMysqlConnection());
            hyberbin.setAdapter(new MysqlAdapter());
            List mapToTableInfo = mapToTableInfo(hyberbin.getMapList("select table_name as `TABLE_NAME`,table_comment as `TABLE_COMMENT` from information_schema.tables where table_schema=database() order by create_time desc"));
            return mapToTableInfo;
        } catch (SQLException ex) {
            log.error("getTableList error", ex);
        }
        return null;
    }

    /**
     * 按照表名称模糊搜索表.
     *
     * @param search 表名称信息
     * @return 表信息列表
     */
    @Override
    public List<TableInfo> getTableList(String search) {
        try {
            DataBaseManager hyberbin = new DataBaseManager(Constants.getMysqlConnection());
            hyberbin.setAdapter(new MysqlAdapter());
            search = search.trim();
            StringBuilder sql = new StringBuilder("select table_name as `table_name`,table_comment as `table_comment` from information_schema.tables where table_schema=database() ");
            if (search.contains(" ")) {
                sql.append(" and table_name in('").append(search.replace(" ", "','")).append("')");
            } else {
                sql.append(" and table_name like ? ");
                hyberbin.addParmeter("%" + search + "%");
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
     *
     * @param tables 表名称信息
     * @return 表信息列表
     */
    @Override
    public List<TableInfo> getTableList(String[] tables) {
        try {
            if (ObjectHelper.isEmpty(tables)) {
                return new ArrayList();
            }
            DataBaseManager hyberbin = new DataBaseManager(Constants.getMysqlConnection());
            hyberbin.setAdapter(new MysqlAdapter());
            StringBuilder sql = new StringBuilder(" and table_name in('none'");
            for (String table : tables) {
                sql.append(",'").append(table).append("'");
            }
            sql.append(")");
            List mapToTableInfo = mapToTableInfo(hyberbin.getMapList("select table_name as `table_name`,table_comment as `table_comment` from information_schema.tables where table_schema=database() " + sql.toString()));

            return mapToTableInfo;
        } catch (SQLException ex) {
            log.error("getTableList error", ex);
        }
        return null;
    }


    /**
     * 根据表的基本信息获得表的完整信息.
     *
     * @param tableBean 带有表基本信息的tableBean
     */
    @Override
    public void getTableInfo(TableBean tableBean) {
        try {
            DataBaseManager hyberbin = new DataBaseManager(Constants.getMysqlConnection());
            hyberbin.setAdapter(new MysqlAdapter());
            List<Map> mapList = hyberbin.getMapList("select character_maximum_length as length ,column_key, column_name as `column`, column_comment as `comment`,CONVERT(data_type USING utf8) as DATA_TYPE,extra from information_schema.columns  where table_schema=database() and table_name='" + tableBean.getTableName() + "'");
            List<ColumnModel> columnList = new ArrayList();
            for (Map map : mapList) {
                ColumnModel columnModel = new ColumnModel();
                String column = ((String) map.get("COLUMN_NAME"));
                columnModel.setColumnName(column);
                String comment = (String) map.get("COLUMN_COMMENT");
                Boolean translate = SqliteUtil.getBoolProperty(Constants.IS_TRANSLATE);
                Boolean isCamel = SqliteUtil.getBoolProperty(Constants.IS_CAMEL);
                if (translate) {
                    String english = YouDaoTranslate.getEnglish(comment);
                    if (!StringUtils.isBlank(english)) {
                        columnModel.setFieldName(FieldUtil.INSTANCE.englishToField(english, true));
                    } else {
                        columnModel.setFieldName(FieldUtil.INSTANCE.columnToField(columnModel.getColumnName().toLowerCase(), false));
                    }
                } else if (isCamel) {
                    columnModel.setFieldName(FieldUtil.INSTANCE.columnToField(columnModel.getColumnName().toLowerCase(), false));
                }
                columnModel.setFieldComment(comment);
//                columnModel.setFieldName(FieldUtil.INSTANCE.columnToField(columnModel.getColumnName().toLowerCase(), false));
                System.out.println(map.get("DATA_TYPE"));
                String data_type = map.get("DATA_TYPE").toString();
                columnModel.setDataType(data_type);
                String property = getJavaType((String) map.get("DATA_TYPE"), tableBean.getTableName(), columnModel.getColumnName());
                if (ObjectHelper.isNullOrEmptyString(property)) {
                    log.error(data_type + "找不到对应的java类型！");
                }
                columnModel.setFieldType(property);
                String xtra = (String) map.get("EXTRA");
                Object length = map.get("LENGTH");
                Object column_key = map.get("COLUMN_KEY");
                if (xtra != null && xtra.equals("AUTO_INCREMENT")) {
                    columnModel.setIsAutoIncrement(true);
                }
                if (ObjectHelper.isNotEmpty(length)) {
                    columnModel.setLength(Integer.parseInt(length.toString()));
                }
                if (ObjectHelper.isNotEmpty(column_key)) {
                    if (column_key.toString().equalsIgnoreCase("PRI")) {
                        columnModel.setIsPri(true);
                    }
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
        try {
            String sql = "select '" + tableName + "' as tablename," + keyColumn + " as dm," + englishColumn + " as english," + chinseColumn + " as chinese," + pingyingColumn + " as pingying from " + tableName;
            DataBaseManager hyberbin = new DataBaseManager(new EnumBean(), Constants.getMysqlConnection());
            hyberbin.setAdapter(new MysqlAdapter());
            List List = hyberbin.showList(sql);
            return List;
        } catch (SQLException ex) {
            log.error("getEnumBeans error", ex);
        }
        return null;
    }

    @Override
    public List<Map> getCodeList(String name) {
        try {
            StringBuilder sql = new StringBuilder("select distinct(zdlx) as zdlx from dc_mkztbm ");
            DataBaseManager hyberbin = new DataBaseManager(Constants.getMysqlConnection());
            hyberbin.setAdapter(new MysqlAdapter());
            if (!ObjectHelper.isNullOrEmptyString(name)) {
                sql.append(" where zdlx like ?");
                hyberbin.addParmeter("%" + name + "%");
            }
            sql.append(" limit 0,100");
            List<Map> list = hyberbin.getMapList(sql.toString());
            return list;
        } catch (SQLException ex) {
            log.error("getCodeList error", ex);
        }
        return null;
    }

    @Override
    public List<String> getAllCodeList() {
        try {
            StringBuilder sql = new StringBuilder("select zdlx from dc_mkztbm ");
            IDbManager connection = Constants.getMysqlConnection();
            DataBaseManager hyberbin = new DataBaseManager(connection);
            hyberbin.setAdapter(new MysqlAdapter());
            List<Map> list = hyberbin.getMapList(sql.toString());

            List codeList = new ArrayList();
            for (Map map : list) {
                codeList.add(map.get("zdlx").toString());
            }
            return codeList;
        } catch (SQLException ex) {
            log.error("getAllCodeList error", ex);
        }
        return null;
    }

    public String getCamelName(String columnName) {
        String[] split = columnName.split("_");
        if (split.length > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                String tempTableName = split[i].substring(0, 1).toLowerCase()
                        + split[i].substring(1, split[i].length());
                sb.append(tempTableName);
            }
            String s = sb.toString();
            s = s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
            return s;
        }
        String tempTables = split[0].substring(0, 1).toUpperCase() + split[0].substring(1, split[0].length());
        return tempTables;
    }

    public String getStandardName(String columnName) {
        String[] split = columnName.split("_");
        if (split.length > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                String tempTableName = split[i].substring(0, 1).toLowerCase()
                        + split[i].substring(1, split[i].length());
                sb.append(tempTableName);
            }
            String s = sb.toString();
            s = s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
            return s;
        }
        String tempTables = split[0].substring(0, 1).toUpperCase() + split[0].substring(1, split[0].length());
        return tempTables;
    }

    @Override
    public List<CodeEnumBean> getCodeEnumBeans(String sjx) {
        try {
            String sql = "select zt.ZDLX as zdlx,zt.sjx as sjx,zt.sjxms as sjxms,yy.yyms as yyms from dc_mkztbm zt,dc_ptyyz yy where zt.sjxmc=yy.ptyykz AND yy.ptyy='en' and zt.zdlx=?";
            IDbManager connection = Constants.getMysqlConnection();
            DataBaseManager hyberbin = new DataBaseManager(new CodeEnumBean(), connection);
            hyberbin.setAdapter(new MysqlAdapter());
            hyberbin.addParmeter(sjx);
            List<CodeEnumBean> list = hyberbin.showList(sql);
            return list;
        } catch (SQLException ex) {
            log.error("getCodeEnumBeans error", ex);
        }
        return null;
    }
}
