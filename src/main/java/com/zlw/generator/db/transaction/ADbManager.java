package com.zlw.generator.db.transaction;

import com.zlw.generator.db.config.DbConfig;
import com.zlw.generator.db.config.IConfigurator;
import com.zlw.generator.db.config.SimpleConfigurator;
import com.zlw.generator.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public abstract class ADbManager implements IDbManager {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    protected Connection connection;
    protected String defaultConfig = "default";
    protected IConfigurator configurator;
    protected DbConfig defaultDbConfig;

    public ADbManager(String defaultConfig) {
        this.configurator = SimpleConfigurator.INSTANCE;
        this.defaultConfig = defaultConfig;
        this.defaultDbConfig = this.configurator.getDbConfig(defaultConfig);
    }

    @Override
    public DbConfig getDefaultDbConfig() {
        return this.defaultDbConfig;
    }

    @Override
    public Connection getConnection() {
        return this.getConnection((String) this.defaultConfig);
    }


    protected Connection getConnection(String name) {
        return this.getConnection((DbConfig) this.configurator.getDbConfig(name));
    }

    protected Connection getConnection(DbConfig config) {
        return this.getConnection(config.getDriver(), config.getUrl(), config.getUsername(), config.getPassword());
    }


    protected Connection getConnection(String driver, String url, String username, String password) {
//        NullUtils.validateNull(driver, "driver");
//        NullUtils.validateNull(driver, "url");
//        NullUtils.validateNull(driver, "username");
//        NullUtils.validateNull(driver, "password");
        this.log.debug("创建数据库连接 driver:{} url:{} username:{} password:{}", new Object[]{driver, url, username, password});

        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, username, password);
            this.openTransaction();
        } catch (ClassNotFoundException var6) {
            this.log.error("数据库连接错误\t找不到驱动", var6);
        } catch (SQLException var7) {
            this.log.error("数据库连接错误\t", var7);
        }

        return this.connection;
    }

    @Override
    public void setConfigurator(IConfigurator configurator) {
        this.configurator = configurator;
    }

    @Override
    public void commit() throws SQLException {
        if (this.connection != null && this.configurator.tranceaction()) {
            this.connection.commit();
            this.log.debug("commit");
        }

    }

    @Override
    public void openTransaction() throws SQLException {
        if (!this.connection.isClosed() && this.configurator.tranceaction() && this.connection.getAutoCommit()) {
            this.log.debug("open Transaction,setAutoCommit false");
            this.connection.setAutoCommit(false);
        }

    }

    @Override
    public void rollBack() throws SQLException {
        if (!this.connection.isClosed() && this.configurator.tranceaction()) {
            this.log.debug("transaction rollback");
            this.connection.rollback();
        }

    }

    @Override
    public void finalCloseConnection() throws SQLException {
        this.log.debug("finalCloseConnection do nothing!");
    }

    @Override
    public IDbManager newInstance() {
        return (IDbManager) ReflectionUtils.instance(this.getClass().getName(), new Class[]{String.class}, new Object[]{this.defaultConfig});
    }

}
