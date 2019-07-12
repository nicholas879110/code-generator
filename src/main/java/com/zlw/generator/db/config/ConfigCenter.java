package com.zlw.generator.db.config;

import com.zlw.generator.db.adapter.IAdapter;
import com.zlw.generator.db.adapter.MysqlAdapter;
import com.zlw.generator.db.crud.DatabaseAccess;
import com.zlw.generator.db.transaction.IDbManager;
import com.zlw.generator.db.transaction.SimpleManager;
import com.zlw.generator.db.util.ISqlout;
import com.zlw.generator.db.util.SimpleSqlout;
import com.zlw.generator.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class ConfigCenter {
    private static final Logger log = LoggerFactory.getLogger(ConfigCenter.class);
    public static ConfigCenter INSTANCE = new ConfigCenter();
    private IConfigurator configurator;
    private IDbManager manager;
    private String sqlout;
    private String adapter;
    private ISqlout sqloutInstance;

    private ConfigCenter() {
        this.configurator = SimpleConfigurator.INSTANCE;
        log.info("use configurator:{}", this.configurator.getClass().getName());
        this.manager = new SimpleManager("default");
        log.info("use database manager:{}", this.manager);
        this.adapter = MysqlAdapter.class.getName();
        log.info("use database adapter:{}", this.adapter);
        this.sqlout = SimpleSqlout.class.getName();
        log.info("use sqlout adapter:{}", this.sqlout);
    }

    public void setConfigurator(IConfigurator configurator) {
        this.configurator = configurator;
        log.info("use configurator:{}", configurator.getClass().getName());
    }

    public IConfigurator getConfigurator() {
        return this.configurator;
    }

    public void setManager(String manager, String configName) {
        this.manager = (IDbManager) ReflectionUtils.instance(manager, new Class[]{String.class}, new Object[]{configName});
        log.info("use database manager:{},defaultConfigName", manager, configName);
    }

    public void setManager(IDbManager manager) {
        this.manager = manager;
        log.info("use database manager:{}", manager);
    }

    public IDbManager getManager() {
        IDbManager manager = this.manager.newInstance();
        manager.setConfigurator(this.getConfigurator());
        return manager;
    }

    public IAdapter getDefaultAdapter() {
        return (IAdapter)ReflectionUtils.instance(this.adapter);
    }

    public DatabaseAccess getDatabase() {
        return new DatabaseAccess(this.getManager());
    }

    public ISqlout getSqlout() {
        if(this.sqloutInstance == null) {
            this.sqloutInstance = (ISqlout)ReflectionUtils.instance(this.sqlout);
        }

        return this.sqloutInstance;
    }

    public void setAdapter(Class<? extends IAdapter> adapter) {
        this.adapter = adapter.getName();
        log.trace("setAdapter {}", this.adapter);
    }

    public void setAdapter(String adapter) {
        this.adapter = adapter;
        log.trace("setAdapter {}", this.adapter);
    }

    public void setSqlout(ISqlout sqlout) {
        this.sqlout = sqlout.getClass().getName();
        log.trace("setSqlout {}", sqlout.getClass().getName());
    }

    public void setSqlout(String sqlout) {
        this.sqlout = sqlout;
        log.trace("setSqlout {}", sqlout);
    }

}
