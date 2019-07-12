package com.zlw.generator.db;

import com.zlw.generator.db.domain.CodeEnumBean;
import com.zlw.generator.db.domain.EnumBean;
import com.zlw.generator.db.domain.TableBean;
import com.zlw.generator.db.domain.TableInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public interface IDatabaseInfo {

    /**
     * 默认获取前20张表.
     * @return 表信息列表
     */
    List getTableList();

    /**
     * 按照表名称模糊搜索表.
     * @param search 表名称信息
     * @return 表信息列表
     */
    List<TableInfo> getTableList(String search);

    /**
     * 按照表名称搜索表.
     * @param tables 表名称信息
     * @return 表信息列表
     */
    List<TableInfo> getTableList(String[] tables);

    /**
     * 从Map里面获取TableBean信息.
     * @param map Map
     * @return
     */
    TableBean getTableBean(TableInfo map);

    /**
     * 根据表的基本信息获得表的完整信息.
     * @param map 表的基本信息
     * @return
     */
    TableBean getTableInfo(TableInfo map);

    /**
     * 根据表的基本信息获得表的完整信息.
     * @param tableBean 带有表基本信息的tableBean
     */
    void getTableInfo(TableBean tableBean);

    List<EnumBean> getEnumBeans(String tableName, String keyColumn, String englishColumn, String chinseColumn, String pingyingColumn);

    List<Map> getCodeList(String name);

    List<String> getAllCodeList();

    List<CodeEnumBean> getCodeEnumBeans(String sjx);
}
