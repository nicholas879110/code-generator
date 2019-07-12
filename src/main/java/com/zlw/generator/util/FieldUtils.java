package com.zlw.generator.util;

import com.zlw.generator.db.bean.FieldColumn;
import com.zlw.generator.db.util.CacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.generator.persistence.Column;
import com.zlw.generator.persistence.JoinColumn;
import com.zlw.generator.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/27
 *
 */
public class FieldUtils {
    private static final Logger log = LoggerFactory.getLogger(FieldUtil.class);
    private static final Class[] EMPTY_ARG = new Class[0];

    public FieldUtils() {
    }

    public static Object getFieldValue(Object tablebean, String fieldName) {
        log.trace("in getFieldValue by fieldName");
        return tablebean != null && fieldName != null && !fieldName.trim().equals("")?ReflectionUtils.invokeGetter(tablebean, fieldName):null;
    }

    public static Object getFieldValue(Object tablebean, Class fieldAnnotation) {
        log.trace("in getFieldValue by fieldAnnotation");
        Field[] declaredFields = tablebean.getClass().getDeclaredFields();
        Field[] var3 = declaredFields;
        int var4 = declaredFields.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if(field.isAnnotationPresent(fieldAnnotation)) {
                return getFieldValue(tablebean, (String)field.getName());
            }
        }

        return null;
    }

    public static void setFieldValue(Object tablebean, Class fieldAnnotation, Object object) {
        log.trace("in setFieldValue");
        Field[] declaredFields = tablebean.getClass().getDeclaredFields();
        Field[] var4 = declaredFields;
        int var5 = declaredFields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            if(field.isAnnotationPresent(fieldAnnotation)) {
                setFieldValue(tablebean, (String)field.getName(), object);
            }
        }

    }

    public static Object getFatherFieldValue(Object tablebean, String fieldName) {
        log.trace("in getFatherFieldValue");

        try {
            Field ex = tablebean.getClass().getDeclaredField(fieldName);
            tablebean = getFieldValue(tablebean, (String)fieldName);
            JoinColumn c = (JoinColumn)ex.getAnnotation(JoinColumn.class);
            fieldName = c.name();
            return getFieldValue(tablebean, (String)fieldName);
        } catch (NoSuchFieldException var4) {
            log.error("获得本POJO类外键的值失败！", var4);
        } catch (SecurityException var5) {
            log.error("未知错误！", var5);
        }

        return null;
    }

    public static FieldColumn getFieldColumnByCache(Field field) {
        log.debug("in getFieldColumnByCache");
        return CacheFactory.MINSTANCE.getFieldColumn(field.getDeclaringClass(), field.getName());
    }

    public static FieldColumn getFieldColumn(Field field) {
        log.trace("in getFieldColumn");
        FieldColumn fieldColumn = new FieldColumn();
        fieldColumn.setField(field);
        field.setAccessible(true);
        if(field.isAnnotationPresent(Column.class)) {
            Column getter = (Column)field.getAnnotation(Column.class);
            fieldColumn.setColumn(getter.name());
            fieldColumn.setLength(getter.length());
            fieldColumn.setSqltype(getter.sqltype());
        } else if(field.isAnnotationPresent(Transient.class)) {
            fieldColumn.setIgnore(true);
        } else if(field.isAnnotationPresent(JoinColumn.class)) {
            JoinColumn getter1 = (JoinColumn)field.getAnnotation(JoinColumn.class);
            fieldColumn.setColumn(getter1.name());
            fieldColumn.setSqltype(getter1.sqltype());
        }

        if(ObjectHelper.isNullOrEmptyString(fieldColumn.getColumn())) {
            fieldColumn.setColumn(field.getName());
        }

        Method getter2 = ReflectionUtils.getAccessibleMethod(field.getDeclaringClass(), ReflectionUtils.get(field.getName()), EMPTY_ARG);
        Method setter = ReflectionUtils.getAccessibleMethod(field.getDeclaringClass(), ReflectionUtils.set(field.getName()), new Class[]{field.getType()});
        fieldColumn.setHasGetterAndSetter(getter2 != null && setter != null);
        return fieldColumn;
    }

    public static Object setFieldValue(Object tablebean, String fieldName, Object value) {
        log.trace("in setFieldValue");
        if(value == null) {
            return tablebean;
        } else {
            ReflectionUtils.invokeSetter(tablebean, fieldName, value);
            return tablebean;
        }
    }

    public static Object setFatherFieldValue(Object tablebean, String fieldName, Object value) {
        log.trace("in setFatherFieldValue");
        if(value == null) {
            return tablebean;
        } else {
            Object newInstance = null;
            String newfieldName = null;

            try {
                Field ex = tablebean.getClass().getDeclaredField(fieldName);
                JoinColumn c = (JoinColumn)ex.getAnnotation(JoinColumn.class);
                newfieldName = c.name();
                newInstance = getFieldValue(tablebean, (String)fieldName);
                if(newInstance == null) {
                    newInstance = ex.getType().newInstance();
                    setFieldValue(tablebean, (String)fieldName, newInstance);
                    setFieldValue(newInstance, (String)"primarykey", newfieldName);
                }
            } catch (IllegalAccessException var7) {
                log.error("参数不正确", var7);
            } catch (InstantiationException var8) {
                log.error("创建字段:{}的实例失败！", fieldName, var8);
            } catch (NoSuchFieldException var9) {
                log.error("没有{}字段！", fieldName, var9);
            }

            return setFieldValue(newInstance, (String)newfieldName, value);
        }
    }

    public static void clone(Object src, Object dist) {
        log.trace("in clone");
        if(src != null && dist != null && src.getClass().equals(dist.getClass())) {
            Field[] declaredFields = src.getClass().getDeclaredFields();
            Field[] var3 = declaredFields;
            int var4 = declaredFields.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                setFieldValue(dist, (String)field.getName(), getFieldValue(src, (String)field.getName()));
            }
        }

    }

    public static Field getField(Class clazz, String name) {
        log.trace("in getField by name");
        return ReflectionUtils.getAccessibleField(clazz, name);
    }

    public static Field getField(Class clazz, Class annotation) {
        log.trace("in getField by annotation");
        Field[] declaredFields = clazz.getDeclaredFields();
        Field[] var3 = declaredFields;
        int var4 = declaredFields.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if(field.isAnnotationPresent(annotation)) {
                return field;
            }
        }

        return null;
    }
}
