package com.zlw.generator.db.transaction;

import java.sql.SQLException;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class SimpleManager extends ADbManager {

    public SimpleManager(String defaultConfig) {
        super(defaultConfig);
    }

    @Override
    public void closeConnection() throws SQLException {
        this.commit();
        this.log.debug("close Connection");
        this.connection.close();
    }
}
