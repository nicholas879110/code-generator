package com.zlw.generator.db.adapter;

import com.zlw.generator.db.page.Pager;

import java.sql.*;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class MysqlAdapter extends AAdapter {

    private static final char[] QUOTE = new char[]{'`', '`'};

    public MysqlAdapter() {
    }

    @Override
    public ResultSet findList(Connection connection, String sql) throws SQLException {
        Statement stm = this.createStatement(connection, sql);
        return stm instanceof PreparedStatement ? ((PreparedStatement) stm).executeQuery() : stm.executeQuery(sql);
    }

    @Override
    public ResultSet findPageList(Connection connection, String sql, Pager pager) throws SQLException {
        return this.findList(connection, sql + " limit " + pager.getTop() + "," + pager.getSize());
    }

    @Override
    public ResultSet findSingle(Connection connection, String sql) throws SQLException {
        return this.findList(connection, sql + " limit 1");
    }

    @Override
    public Object findUnique(Connection connection, String sql) throws SQLException {
        ResultSet singel = this.findSingle(connection, sql);
        return singel != null && singel.next() ? singel.getObject(1) : null;
    }

    @Override
    public int update(Connection connection, String sql) throws SQLException {
        Statement stm = this.createStatement(connection, sql);
        return stm instanceof PreparedStatement ? ((PreparedStatement) stm).executeUpdate() : stm.executeUpdate(sql);
    }

    @Override
    public int delete(Connection connection, String sql) throws SQLException {
        Statement stm = this.createStatement(connection, sql);
        return stm instanceof PreparedStatement ? ((PreparedStatement) stm).executeUpdate() : stm.executeUpdate(sql);
    }

    @Override
    public char[] getQuote() {
        return QUOTE;
    }
}
