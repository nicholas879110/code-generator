package com.zlw.generator.db.adapter;

import com.zlw.generator.db.page.Pager;
import com.zlw.generator.util.NumberUtils;

import java.sql.*;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class OracleAdapter extends AAdapter {

    private static final char[] QUOTE = new char[]{'\"', '\"'};

    public OracleAdapter() {
    }

    @Override
    public char[] getQuote() {
        return QUOTE;
    }

    @Override
    public ResultSet findList(Connection connection, String sql) throws SQLException {
        Statement statement = this.createStatement(connection, sql);
        return statement instanceof PreparedStatement ? ((PreparedStatement) statement).executeQuery() : statement.executeQuery(sql);
    }

    @Override
    public ResultSet findPageList(Connection connection, String sql, Pager pager) throws SQLException {
        sql = "select * from ( select row_.*, rownum rownum_ from ( " + sql + ") row_ where rownum <= " + (pager.getTop().intValue() + pager.getSize().intValue()) + ") where rownum_ > " + pager.getTop();
        return this.findList(connection, sql);
    }

    @Override
    public ResultSet findSingle(Connection connection, String sql) throws SQLException {
        return this.findList(connection, "select res.*,rownum from (" + sql + ") res where rownum=1");
    }

    @Override
    public Object findUnique(Connection connection, String sql) throws SQLException {
        ResultSet singel = this.findSingle(connection, sql);
        return singel != null && singel.next() ? singel.getObject(1) : null;
    }

    @Override
    public int update(Connection connection, String sql) throws SQLException {
        Statement statement = this.createStatement(connection, sql);
        return statement instanceof PreparedStatement ? ((PreparedStatement) statement).executeUpdate() : statement.executeUpdate(sql);
    }

    @Override
    public int delete(Connection connection, String sql) throws SQLException {
        Statement statement = this.createStatement(connection, sql);
        return statement instanceof PreparedStatement ? ((PreparedStatement) statement).executeUpdate() : statement.executeUpdate(sql);
    }

    @Override
    public int getCount(Connection connection, String sql) throws SQLException {
        sql = "select count(*) from (" + sql + ")";
        Object findUnique = this.findUnique(connection, sql);
        return NumberUtils.parseInt(findUnique);
    }
}
