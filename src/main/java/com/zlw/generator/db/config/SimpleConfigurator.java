package com.zlw.generator.db.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class SimpleConfigurator implements IConfigurator {

    private static final Map<String, DbConfig> DBCONFIGS = Collections.synchronizedMap(new HashMap());
    public static final SimpleConfigurator INSTANCE = new SimpleConfigurator();

    private SimpleConfigurator() {
    }

    public static void addConfigurator(DbConfig config) {
        DBCONFIGS.put(config.getConfigName(), config);
    }

    @Override
    public DbConfig getDefaultConfig() {
        return (DbConfig) DBCONFIGS.get("default");
    }

    @Override
    public DbConfig getDbConfig(String name) {
        return (DbConfig) DBCONFIGS.get(name);
    }

    @Override
    public boolean sqlOut() {
        return true;
    }

    @Override
    public boolean prepare() {
        return true;
    }

    @Override
    public boolean tranceaction() {
        return true;
    }

    static {
        addConfigurator(new DbConfig());
    }
}
