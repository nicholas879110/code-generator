package com.zlw.generator.db.crud;

import com.zlw.generator.db.adapter.IAdapter;
import com.zlw.generator.db.adapter.MysqlAdapter;
import com.zlw.generator.db.adapter.OracleAdapter;
import com.zlw.generator.db.config.ConfigCenter;
import com.zlw.generator.db.transaction.IDbManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public abstract class BaseDbTool {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected IAdapter adapter;
    protected IDbManager tx;

    protected BaseDbTool(IDbManager tx) {
        this.tx = tx;
        String driver = tx.getDefaultDbConfig().getDriver();
        if(driver.contains("oracle")) {
            this.adapter = new OracleAdapter();
        } else if(driver.contains("mysql")) {
            this.adapter = new MysqlAdapter();
        } else if(driver.contains("sqlite")) {
            this.adapter = new MysqlAdapter();
        } else if(driver.contains("sqlserver")) {
//            this.adapter = new SqlserverAdapter();
        } else {
            this.adapter = ConfigCenter.INSTANCE.getDefaultAdapter();
        }

    }

    protected Connection getConnection() {
        return this.tx.getConnection();
    }

    public IAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter(IAdapter adapter) {
        this.adapter = adapter;
    }
}
