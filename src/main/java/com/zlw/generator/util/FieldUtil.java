package com.zlw.generator.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class FieldUtil extends FieldUtils{

    public static FieldUtil INSTANCE=new FieldUtil();
    /**
     * 将字段名的_去掉后面的字母大写.
     * @param column 字段名.
     * @param firstUp
     * @return
     */
    public  String columnToField(String column, boolean firstUp) {
        char[] toCharArray = column.trim().toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] == '_' && i != toCharArray.length - 1) {
                toCharArray[i + 1] = (toCharArray[i + 1] + "").toUpperCase().charAt(0);
            }
        }
        column = new String(toCharArray).replace("_", "");
        return firstUp ? firstUp(column) : firstLower(column);
    }

    public  String englishToField(String column, boolean firstUp) {
        char[] toCharArray = column.trim().toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] == ' ' && i != toCharArray.length - 1) {
                toCharArray[i + 1] = (toCharArray[i + 1] + "").toUpperCase().charAt(0);
            }
        }
        column = new String(toCharArray).replace(" ", "");
        return firstUp ? firstUp(column) : firstLower(column);
    }

    public Set<String> getAllFields(Object obj) {
        Set<String> fields = new HashSet<String>();
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] field = superClass.getDeclaredFields();
            for (Field f : field) {
                fields.add(f.getName());
            }
        }
        return fields;
    }

    /**
     * 生成字段的getter方法名.
     * @param field 字段名.
     * @return
     */
    public  String getter(String field) {
        if(ObjectHelper.isNullOrEmptyString(field)){
            return "";
        }
        return "get" + (field.charAt(0) + "").toUpperCase() + field.substring(1);
    }

    /**
     * 生成字段的setter方法名.
     * @param field 字段名.
     * @return
     */
    public  String setter(String field) {
        if(ObjectHelper.isNullOrEmptyString(field)){
            return "";
        }
        return "set" + (field.charAt(0) + "").toUpperCase() + field.substring(1);
    }

    public  String firstUp(String str) {
        if(ObjectHelper.isNullOrEmptyString(str)){
            return "";
        }
        return (str.charAt(0) + "").toUpperCase() + str.substring(1);
    }

    public  String firstLower(String str) {
        if(ObjectHelper.isNullOrEmptyString(str)){
            return "";
        }
        return (str.charAt(0) + "").toLowerCase() + str.substring(1);
    }
}
