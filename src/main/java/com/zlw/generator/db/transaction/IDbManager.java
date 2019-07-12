package com.zlw.generator.db.transaction;

import com.zlw.generator.db.config.DbConfig;
import com.zlw.generator.db.config.IConfigurator;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public interface IDbManager {

    Connection getConnection();

    void openTransaction() throws SQLException;

    void commit() throws SQLException;

    void rollBack() throws SQLException;

    void closeConnection() throws SQLException;

    void finalCloseConnection() throws SQLException;

    void setConfigurator(IConfigurator var1);

    IDbManager newInstance();

    DbConfig getDefaultDbConfig();
}
