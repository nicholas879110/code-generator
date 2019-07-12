package com.zlw.generator.db.crud;

import com.zlw.generator.db.transaction.IDbManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class DatabaseAccess extends BaseDbTool {

    public DatabaseAccess(IDbManager tx) {
        super(tx);
    }

    public DatabaseAccess setParmeter(Object parameter) {
        this.log.trace("addParmeter {}", parameter);
        this.adapter.addParameter(parameter);
        return this;
    }

    public DatabaseAccess clearParmeter() {
        this.adapter.getParmeters().clear();
        return this;
    }

    public int update(String sql) throws SQLException {
        int update = this.adapter.update(this.getConnection(), sql);
        this.tx.closeConnection();
        return update;
    }

    public int delete(String sql) throws SQLException {
        int delete = this.adapter.delete(this.getConnection(), sql);
        this.tx.closeConnection();
        return delete;
    }

    public ResultSet query(String sql) throws SQLException {
        ResultSet findList = this.adapter.findList(this.getConnection(), sql);
        this.tx.closeConnection();
        return findList;
    }

    public ResultSet querySingle(String sql) throws SQLException {
        ResultSet findSingle = this.adapter.findSingle(this.getConnection(), sql);
        this.tx.closeConnection();
        return findSingle;
    }

    public Object queryUnique(String sql) throws SQLException {
        Object findUnique = this.adapter.findUnique(this.getConnection(), sql);
        this.tx.closeConnection();
        return findUnique;
    }
}
