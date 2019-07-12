package com.zlw.generator.db.adapter;

import com.zlw.generator.db.bean.FieldColumn;
import com.zlw.generator.db.page.Pager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public interface IAdapter {

    void addParameter(Object var1);

    void addParameter(Object var1, FieldColumn var2);

    List getParmeters();

    ResultSet findList(Connection var1, String var2) throws SQLException;

    ResultSet findPageList(Connection var1, String var2, Pager var3) throws SQLException;

    ResultSet findSingle(Connection var1, String var2) throws SQLException;

    Object findUnique(Connection var1, String var2) throws SQLException;

    int getCount(Connection var1, String var2) throws SQLException;

    int update(Connection var1, String var2) throws SQLException;

    int delete(Connection var1, String var2) throws SQLException;

    char[] getQuote();

    void sqlout();
}
