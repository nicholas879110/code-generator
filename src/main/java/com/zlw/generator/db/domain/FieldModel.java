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

import java.util.Collection;

/**
 * FieldModel说明.
 * @author Hyberbin
 * @date 2013-6-28 10:40:56
 */
public class FieldModel{
    /**字段上的注解列表*/
    private Collection<String> annotationList;
    /**字段上的注释*/
    private String fieldComment;
    /**字段本的的字符串表现形式*/
    private String fieldBody;
    /**字段名*/
    private String fieldName;
    /**字段的java类型*/
    private String fieldType;
    //下面几个字段主要是跟郭可一致
    private String chinese;
    private String javaType;
    private String fieldName4ter;

    public Collection<String> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(Collection<String> annotationList) {
        this.annotationList = annotationList;
    }

    public String getFieldName4ter() {
        return fieldName4ter;
    }

    public void setFieldName4ter(String fieldName4ter) {
        this.fieldName4ter = fieldName4ter;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        if(fieldComment==null){
            fieldComment=fieldName;
        }
        chinese=fieldComment.toLowerCase();
        this.fieldComment = fieldComment;
    }

    public String getFieldBody() {
        return fieldBody;
    }

    public void setFieldBody(String fieldBody) {
        this.fieldBody = fieldBody;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        fieldName4ter=FieldUtil.INSTANCE.firstUp(fieldName);
        this.fieldName = FieldUtil.INSTANCE.firstLower(fieldName);
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        javaType=fieldType;
        this.fieldType = fieldType;
    }
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())||fieldName==null) {
            return false;
        }
        return fieldName.equals(((FieldModel) obj).getFieldName());
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getChinese() {
        return chinese;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.fieldName != null ? this.fieldName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return fieldName; //To change body of generated methods, choose Tools | Templates.
    }
    
   public boolean isVersion(){
       return "bbh".equals(fieldName)||"version".equals(fieldName.toString());
   }
   public boolean isDate(){
       if(fieldType==null){
           return false;
       }
        String toLowerCase = fieldType.toLowerCase();
       return toLowerCase.contains("date")||toLowerCase.contains("time");
   }
}
