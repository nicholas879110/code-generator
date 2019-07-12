package com.zlw.generator.db.config;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public interface IConfigurator {

    DbConfig getDefaultConfig();

    DbConfig getDbConfig(String var1);

    boolean sqlOut();

    boolean prepare();

    boolean tranceaction();
}
