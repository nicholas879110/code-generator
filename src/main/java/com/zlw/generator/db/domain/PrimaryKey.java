package com.zlw.generator.db.domain;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/10/20
 *
 */
public class PrimaryKey {

    private String priKey;//主键数据库字段
    private String priField;// 主键字段名 驼峰命名字段
    private String upperField;//首字母大写字段
    private String priDataType;//主键java类型

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public String getPriField() {
        return priField;
    }

    public void setPriField(String priField) {
        this.priField = priField;
    }

    public String getPriDataType() {
        return priDataType;
    }

    public void setPriDataType(String priDataType) {
        this.priDataType = priDataType;
    }

    public String getUpperField() {
        return upperField;
    }

    public void setUpperField(String upperField) {
        this.upperField = upperField;
    }
}
