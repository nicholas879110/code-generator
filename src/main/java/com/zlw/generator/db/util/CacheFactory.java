package com.zlw.generator.db.util;

import com.zlw.generator.db.bean.FieldColumn;
import com.zlw.generator.db.bean.TableBean;
import com.zlw.generator.util.FieldUtil;
import com.zlw.generator.util.ObjectHelper;
import com.zlw.generator.util.ReflectionUtils;
import org.slf4j.Logger;

import com.zlw.generator.persistence.Id;
import com.zlw.generator.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class CacheFactory {

    private  final Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    public static CacheFactory MINSTANCE = new CacheFactory();
    private final Map<Class, TableBean> hyberbinMap = Collections.synchronizedMap(new HashMap());
    private final Map<Class, Map<String, Method>> methodMap = Collections.synchronizedMap(new HashMap());
    private final Map<Class, Map<String, Field>> fieldMap = Collections.synchronizedMap(new HashMap());

    private CacheFactory() {
    }

    private void putHyberbin(Class po, TableBean tableBean) {
        log.trace("in putHyberbin");
        this.hyberbinMap.put(po, tableBean);
        log.debug("存放一个PO类信息到hyberbinMap，po:{}", po.getName());
    }

    public TableBean getHyberbin(Class po, boolean superField) {
        log.trace("in getHyberbin");
        TableBean tableBean = (TableBean)this.hyberbinMap.get(po);
        if(tableBean == null) {
            tableBean = new TableBean();
            if(po.isAnnotationPresent(Table.class)) {
                Table declaredFields = (Table)po.getAnnotation(Table.class);
                tableBean.setTableName(declaredFields.name());
            } else {
                tableBean.setTableName(po.getSimpleName());
            }

            Field[] var10 = superField?(Field[]) ReflectionUtils.getAllFields(po).toArray(new Field[0]):po.getDeclaredFields();
            ArrayList columns = new ArrayList(0);
            Field[] var6 = var10;
            int var7 = var10.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Field field = var6[var8];
                columns.add(FieldUtil.getFieldColumn(field));
                if(field.isAnnotationPresent(Id.class)) {
                    tableBean.setPrimaryKey(field.getName());
                }
            }

            tableBean.setColumns(columns);
            this.putHyberbin(po, tableBean);
        }

        log.trace("out getHyberbin");
        return tableBean;
    }

    public FieldColumn getFieldColumn(Class po, String fieldName) {
        log.debug("in getFieldColumn po:{},fieldName:{}",po,fieldName);
        TableBean hyberbin = this.getHyberbin(po, true);
        return (FieldColumn)hyberbin.getColumnMap().get(fieldName);
    }

    public void putMethod(Object o, Class<?>[] types, Method method) {
        log.trace("in putMethod");
        Class clazz = o instanceof Class?(Class)o:o.getClass();
        Map objectMethodMap = (Map)this.methodMap.get(clazz);
        if(objectMethodMap == null) {
            objectMethodMap = Collections.synchronizedMap(new HashMap());
            this.methodMap.put(clazz, objectMethodMap);
        }

        String key = method.getName() + this.getTypes(types);
        objectMethodMap.put(key, method);
        log.debug("方法缓存加入：{}，key：{}，参数个数:{}", new Object[]{clazz.getName(), key, Integer.valueOf(types.length)});
    }

    private String getTypes(Class<?>[] types) {
        if(ObjectHelper.isEmpty(types)) {
            return "";
        } else {
            StringBuilder type = new StringBuilder();
            Class[] var3 = types;
            int var4 = types.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Class clazz = var3[var5];
                type.append(",").append(clazz.getName());
            }

            return type.toString();
        }
    }

    public Method getMethod(Object o, String name, Class<?>[] types) {
        Class clazz = o instanceof Class?(Class)o:o.getClass();
        log.trace("in getMethod from {}, get:{}", clazz.getName(), name);
        Map objectMethodMap = (Map)this.methodMap.get(clazz);
        if(objectMethodMap != null) {
            return (Method)objectMethodMap.get(name + this.getTypes(types));
        } else {
            log.debug("缓存中没有类：{}，方法名：{},参数个数：{}", new Object[]{clazz.getName(), name, Integer.valueOf(types.length)});
            return null;
        }
    }

    public void putField(Object o, Field field) {
        Class clazz = o instanceof Class?(Class)o:o.getClass();
        log.trace("in putField:put class {},fieldName {}", clazz.getName(), field.getName());
        Map fields = (Map)this.fieldMap.get(clazz);
        if(fields == null) {
            fields = Collections.synchronizedMap(new HashMap());
            this.fieldMap.put(clazz, fields);
        }

        fields.put(field.getName(), field);
        log.trace("out putField");
    }

    public Field getField(Object o, String name) {
        Class clazz = o instanceof Class?(Class)o:o.getClass();
        log.trace("in getField:get class {},fieldName {}", clazz.getName(), name);
        Map fields = (Map)this.fieldMap.get(clazz);
        if(fields != null) {
            return (Field)fields.get(name);
        } else {
            log.debug("缓存中没有对象：{},字段：{}的信息", clazz.getName(), name);
            return null;
        }
    }
}
