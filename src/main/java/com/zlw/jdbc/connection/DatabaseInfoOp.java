package com.zlw.jdbc.connection;


import com.zlw.generator.db.domain.TableInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 数据库信息获得操作类
 */
public class DatabaseInfoOp {

    Connection conn = null;

    private String classDriver;
    private String url;
    private String username;
    private String password;
    private String schema;

    public DatabaseInfoOp(String classDriver, String url, String username, String password) {
        super();
        this.classDriver = classDriver;
        this.url = url;
        this.username = username;
        this.password = password;

        //存储数据库连接
    }

    public DatabaseInfoOp(String classDriver, String url, String username, String password, String schema) {
        super();
        this.classDriver = classDriver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.schema = schema;
    }

    public Connection getConnectionByJDBC() {
        try {
            // 装载驱动包类
            Class.forName(classDriver);
            // 加载驱动
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("装载驱动包出现异常!请查正！");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("链接数据库发生异常!");
            e.printStackTrace();
        }
        return conn;
    }

//    public List<Map> getMapList(String sql, Object... parmeters) throws SQLException {
//        List mapList = this.getMapList(this.adapter.findList(this.getConnection(), sql));
//        this.tx.closeConnection();
//        return mapList;
//    }


    public List  getTableList() {
        try {
            // 装载驱动包类
            //Class.forName(classDriver);
            // 加载驱动
            conn = getConnectionByJDBC();
            String searchTableSql="select TABLE_name as \"table\"  ,comments as \"COMMENT\"  from USER_tab_comments where INSTR(TABLE_NAME,'$')=0 and ROWNUM<20 ";
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, null, new String[]{"TABLE"/*, "VIEW"*/});

            // String tableName = "";
            List<TableInfo> tableInfos=new ArrayList<TableInfo>();
            while (rs.next()) {
                TableInfo table = new TableInfo();

                table.setTablename(rs.getString("TABLE_NAME"));
                table.setTablecomment(rs.getString("REMARKS"));
//                table.setTableSchem(rs.getString(1));
                tableInfos.add(table);
            }
//            Statement statement = conn.this.createStatement(conn, searchTableSql);
//            return statement instanceof PreparedStatement?((PreparedStatement)statement).executeQuery():statement.executeQuery(sql);

//            Hyberbin hyberbin = new Hyberbin(Constants.getOracleConnection());
//            List mapToTableInfo = mapToTableInfo(hyberbin.getMapList("select TABLE_name as \"table\"  ,comments as \"COMMENT\"  from USER_tab_comments where INSTR(TABLE_NAME,'$')=0 and ROWNUM<20 "));
            return tableInfos;
        } catch (Exception ex) {
//            log.error("getTableList error", ex);
        }
        return null;
    }

    /**
     * 获得表数据
     *
     * @return
     * @throws java.sql.SQLException
     * @Title: getDbInfo
     *//*
    public Database getDbInfo(String tableNamePattern) throws SQLException {

        Database databaseBean = new Database();

        // 表队列
        List<TableInfo> tableList = new ArrayList<TableInfo>();

        // 初始化数据库
        getConnectionByJDBC();

        // 获取数据库信息
        DatabaseMetaData dbmd = conn.getMetaData();

        databaseBean.setDatabaseProductName(dbmd.getDatabaseProductName());

        // 获得数据库表
        ResultSet rs = dbmd.getTables(null, null, tableNamePattern, new String[]{"TABLE", "VIEW"});

        // String tableName = "";
        while (rs.next()) {
            TableInfo table = new TableInfo();

            table.setTableName(rs.getString("TABLE_NAME"));
            table.setTableComment(rs.getString("REMARKS"));
            table.setTableSchem(rs.getString(1));

            // 设置列信息
            ResultSet rscol = dbmd.getColumns(null, null, table.getTableName(), null);

            Column tempColumn;

            while (rscol.next()) {
                tempColumn = new Column();
                tempColumn.setColumnName(rscol.getString("COLUMN_NAME"));
                tempColumn.setColumnType(Integer.parseInt(rscol.getString("DATA_TYPE")));

                String remarks = rscol.getString("REMARKS");
                if (remarks.length() < 1)
                    remarks = "";
                tempColumn.setColumnComment(remarks);
                tempColumn.setAutoIncrement(rscol.getString("IS_AUTOINCREMENT").equals("YES"));
                tempColumn.setNullAble(rscol.getString("IS_AUTOINCREMENT").equals("YES"));

                // 添加列到表中
                table.getColumnList().add(tempColumn);

            }

            // 设置主键列
            ResultSet rsPrimary = dbmd.getPrimaryKeys(null, null, table.getTableName());
            while (rsPrimary.next()) {
                if (rsPrimary.getString("COLUMN_NAME") != null) {

                    for (int i = 0; i < table.getColumnList().size(); i++) {
                        Column coltemp = table.getColumnList().get(i);
                        if (coltemp.getColumnName().equals(rsPrimary.getString("COLUMN_NAME"))) {
                            coltemp.setPrimary(true);
                        }
                    }

                }
            }

            // 设置外键列
            ResultSet rsFPrimary = dbmd.getImportedKeys(null, null, table.getTableName());
            while (rsFPrimary.next()) {

                for (int i = 0; i < table.getColumnList().size(); i++) {
                    Column coltemp = table.getColumnList().get(i);
                    if (coltemp.getColumnName().equals(rsFPrimary.getString("FKCOLUMN_NAME"))) {
                        coltemp.setForeignKey(true);
                    }
                }
            }
            tableList.add(table);
        }
        databaseBean.setTableList(tableList);
        return databaseBean;
    }

    public Database getDbInfo() throws SQLException {

        Database databaseBean = new Database();

        // 表队列
        List<TableInfo> tableList = new ArrayList<TableInfo>();

        // 初始化数据库
        getConnectionByJDBC();

        // 获取数据库信息
        DatabaseMetaData dbmd = conn.getMetaData();

        databaseBean.setDatabaseProductName(dbmd.getDatabaseProductName());

        // 获得数据库表
        ResultSet rs = dbmd.getTables(null, this.schema, null, new String[]{"TABLE", "VIEW"});

        // String tableName = "";
        while (rs.next()) {
            TableInfo table = new TableInfo();

            table.setTableName(rs.getString("TABLE_NAME"));
            table.setTableComment(rs.getString("REMARKS"));
            table.setTableSchem(rs.getString(1));

            // 设置列信息
            ResultSet rscol = dbmd.getColumns(null, null, table.getTableName(), null);

            Column tempColumn;

            while (rscol.next()) {
                tempColumn = new Column();
                tempColumn.setColumnName(rscol.getString("COLUMN_NAME"));
                tempColumn.setColumnType(Integer.parseInt(rscol.getString("DATA_TYPE")));

                String remarks = rscol.getString("REMARKS");
                if (remarks.length() < 1)
                    remarks = "";
                tempColumn.setColumnComment(remarks);
                tempColumn.setAutoIncrement(rscol.getString("IS_AUTOINCREMENT").equals("YES"));
                tempColumn.setNullAble(rscol.getString("IS_AUTOINCREMENT").equals("YES"));

                // 添加列到表中
                table.getColumnList().add(tempColumn);

            }

            // 设置主键列
            ResultSet rsPrimary = dbmd.getPrimaryKeys(null, null, table.getTableName());
            while (rsPrimary.next()) {
                if (rsPrimary.getString("COLUMN_NAME") != null) {

                    for (int i = 0; i < table.getColumnList().size(); i++) {
                        Column coltemp = table.getColumnList().get(i);
                        if (coltemp.getColumnName().equals(rsPrimary.getString("COLUMN_NAME"))) {
                            coltemp.setPrimary(true);
                        }
                    }

                }
            }

            // 设置外键列
            ResultSet rsFPrimary = dbmd.getImportedKeys(null, null, table.getTableName());
            while (rsFPrimary.next()) {

                for (int i = 0; i < table.getColumnList().size(); i++) {
                    Column coltemp = table.getColumnList().get(i);
                    if (coltemp.getColumnName().equals(rsFPrimary.getString("FKCOLUMN_NAME"))) {
                        //System.out.println("FKCOLUMN_NAME "+rsFPrimary.getString("FKCOLUMN_NAME"));
                        coltemp.setForeignKey(true);
                    }
                }
            }
            tableList.add(table);

        }

        databaseBean.setTableList(tableList);
        return databaseBean;
    }*/

}
