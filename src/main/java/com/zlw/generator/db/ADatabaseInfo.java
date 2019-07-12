package com.zlw.generator.db;

import com.zlw.generator.db.domain.TableBean;
import com.zlw.generator.db.domain.TableInfo;
import com.zlw.generator.util.LoadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public abstract class ADatabaseInfo implements IDatabaseInfo {
    protected Logger log = LoggerFactory.getLogger(getClass());

    private LoadProperties fieldmapping;
    public ADatabaseInfo(LoadProperties fieldmapping) {
        this.fieldmapping=fieldmapping;
    }

    protected List mapToTableInfo(List<Map> maps) {
        List<TableInfo> list = new ArrayList<TableInfo>();
        log.info("数据库表信息：{}",maps);
        for (Map map : maps) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setTablename(map.get("TABLE_NAME").toString());
            Object comment = map.get("TABLE_COMMENT");
            tableInfo.setTablecomment(comment == null ? null : comment.toString());
            list.add(tableInfo);
        }
        return list;
    }

    /**
     * 从Map里面获取TableBean信息.
     * @param map Map
     * @return
     */
    @Override
    public TableBean getTableBean(TableInfo map) {
        TableBean tableBean = new TableBean();
        tableBean.setTableName(map.getTablename());
        tableBean.setEntityName(map.getEntyname());
        tableBean.setTableComment(map.getTablecomment());
        return tableBean;
    }

    /**
     * 根据表的基本信息获得表的完整信息.
     * @param map 表的基本信息
     * @return
     */
    @Override
    public TableBean getTableInfo(TableInfo map) {
        TableBean tableBean = getTableBean(map);
        getTableInfo(tableBean);
        return tableBean;
    }

    public String getJavaType(String sqlType,String tableName,String columnName){
        Set<Object> keySet = fieldmapping.getProps().keySet();
        for(Object key:keySet){
            if(sqlType.toLowerCase().matches(key.toString())){
                return fieldmapping.getProperty(key.toString());
            }
        }
        throw new IllegalArgumentException("表:"+tableName+"字段:"+columnName+"找不到"+sqlType+"对应的Java类型!");
    }


}
