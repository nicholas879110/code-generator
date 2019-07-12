package com.zlw.generator.db.transaction;

import com.zlw.generator.db.config.DbConfig;
import com.zlw.generator.db.config.IConfigurator;
import com.zlw.generator.util.MD5Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class SingleManager extends ADbManager {

    private static final Map<String, Connection> CONN_NAME_MAP = Collections.synchronizedMap(new HashMap());
    private static final Map<String, Connection> CONN_MD5_MAP = Collections.synchronizedMap(new HashMap());

    public SingleManager(String defaultConfig) {
        super(defaultConfig);
    }

    @Override
    public synchronized IDbManager newInstance() {
        return super.newInstance();
    }

    @Override
    public synchronized void finalCloseConnection() throws SQLException {
        super.connection.close();
        CONN_NAME_MAP.remove(this.getDefaultDbConfig().getConfigName());
        CONN_MD5_MAP.remove(MD5Util.MD5(this.getDefaultDbConfig().getDriver() + this.getDefaultDbConfig().getUrl() + this.getDefaultDbConfig().getUsername() + this.getDefaultDbConfig().getPassword()));
    }

    @Override
    public synchronized void rollBack() throws SQLException {
        super.rollBack();
    }

    @Override
    public synchronized void openTransaction() throws SQLException {
        super.openTransaction();
    }

    @Override
    public synchronized void commit() throws SQLException {
        super.commit();
    }

    @Override
    public synchronized void setConfigurator(IConfigurator configurator) {
        super.setConfigurator(configurator);
    }

    @Override
    protected synchronized Connection getConnection(String driver, String url, String username, String password) {
        String md5 = MD5Util.MD5(driver + url + username + password);
        Connection getByMd5 = (Connection) CONN_MD5_MAP.get(md5);
        Connection getByName = (Connection) CONN_NAME_MAP.get(this.getDefaultDbConfig().getConfigName());

        try {
            if (getByMd5 == null && getByName == null) {
                super.connection = super.getConnection(driver, url, username, password);
                super.connection.close();
                super.connection = super.getConnection(driver, url, username, password);
                CONN_MD5_MAP.put(md5, super.connection);
                CONN_NAME_MAP.put(this.getDefaultDbConfig().getConfigName(), super.connection);
            } else if (getByMd5 != getByName) {
                getByName.close();
                super.connection = super.getConnection(driver, url, username, password);
                CONN_MD5_MAP.put(md5, super.connection);
                CONN_NAME_MAP.put(this.getDefaultDbConfig().getConfigName(), super.connection);
            } else {
                super.connection = (Connection) CONN_NAME_MAP.get(this.getDefaultDbConfig().getConfigName());
            }
        } catch (SQLException var9) {
            this.log.error("获取链接失败!", var9);
        }

        return super.connection;
    }

    @Override
    protected synchronized Connection getConnection(DbConfig config) {
        return super.getConnection(config);
    }

    @Override
    protected synchronized Connection getConnection(String name) {
        return super.getConnection(name);
    }

    @Override
    public synchronized Connection getConnection() {
        return super.getConnection();
    }

    @Override
    public synchronized DbConfig getDefaultDbConfig() {
        return super.getDefaultDbConfig();
    }

    @Override
    public synchronized void closeConnection() throws SQLException {
        this.commit();
    }
}
