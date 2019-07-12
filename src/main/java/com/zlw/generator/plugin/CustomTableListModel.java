package com.zlw.generator.plugin;


import com.zlw.generator.db.Constants;
import com.zlw.generator.db.domain.TableInfo;
import com.zlw.generator.db.util.DbUtils;
import com.zlw.generator.db.util.SqliteUtil;
import com.zlw.generator.util.FieldUtil;
import com.zlw.generator.util.ObjectHelper;
import com.zlw.generator.util.YouDaoTranslate;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/6/30
 *
 */
public class CustomTableListModel extends DefaultTableModel {

    /* 常量配置区*/

    private final String[] tableHeader = {"选择", "表名", "英文名", "说明"};
    private final boolean[] isEditAble = {true, false, true, true};
    private List<TableInfo> tableList;

    public List<TableInfo> getTableList() {
        return tableList;
    }

    /**
     * 默认情况下这个方法不用重新实现的，但是这样就会造成如果这个列式boolean的类型，就当做string来处理了
     * 如果是boolean的类型那么用checkbox来显示
     *
     * @return
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        } else {
            return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return isEditAble[column]; //To change body of generated methods, choose Tools | Templates.
    }


    public CustomTableListModel() {
    }

    /**
     * 加一个列表
     *
     * @param list
     */
    public synchronized void addTableList(final List<TableInfo> list) {
        this.setColumnIdentifiers(tableHeader);
        this.getDataVector().clear();
        this.tableList = list;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (TableInfo tableInfo : list) {
                    try {
                        TableInfo newtableInfo = getTableInfo(tableInfo.getTablename(), tableInfo.getTablecomment());
                        tableInfo = newtableInfo == null ? tableInfo : newtableInfo;
                        Object[] row = new Object[4];
                        row[0] = false;
                        row[1] = tableInfo.getTablename();
                        row[2] = tableInfo.getEntyname();
                        row[3] = tableInfo.getTablecomment();
                        list.set(i++, tableInfo);
                        addRow(row);
                    } catch (SQLException ex) {
//                        SwingLogUtil.showError(ex);
                        Logger.getLogger(CustomTableListModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        thread.start();
    }


    /**
     * 设置全选
     */
    public void selectAll() {
        for (int i = 0; i < this.getRowCount(); i++) {
            boolean b = (Boolean) getValueAt(i, 0);
            this.setValueAt(!b, i, 0);
        }
    }

    /**
     * 选中所有表
     */
    public void selectAllTable() {
        for (int i = 0; i < this.getRowCount(); i++) {
            this.setValueAt(true, i, 0);
        }
    }

    /**
     * 全不选
     */
    public void unselectAll() {
        for (int i = 0; i < this.getRowCount(); i++) {
            this.setValueAt(false, i, 0);
        }
    }

    /**
     * 获得选择了的表
     *
     * @return
     */
    public List<TableInfo> getSelectedList() {
        List<TableInfo> list = new ArrayList();
        for (int i = 0; i < this.getRowCount(); i++) {
            if ((Boolean) getValueAt(i, 0)) {
                list.add(tableList.get(i));
            }
        }
        return list;
    }

    public void reTranslate(List<TableInfo> tableList) throws SQLException {
        List<TableInfo> selectedList = getSelectedList();
        String[] tables = new String[selectedList.size()];
        int i = 0;
        for (TableInfo map : selectedList) {
            tables[i++] = map.getTablename();
        }
        DbUtils.clearTableEnglish(Constants.getSqliteConnection(), tables);
        addTableList(tableList);
    }

    private TableInfo getTableInfo(String tableName, String tableComment) throws SQLException {
        TableInfo info = DbUtils.getInfoByName(tableName, Constants.getSqliteConnection());
        if (ObjectHelper.isNullOrEmptyString(tableComment)) {//注释为空就以表名做注释
            tableComment = tableName;
        }
        if (info == null || ObjectHelper.isNullOrEmptyString(info.getEntyname())) {//之前没有缓存数据
            info = new TableInfo();
            Boolean translate = SqliteUtil.getBoolProperty(Constants.IS_TRANSLATE);
            Boolean isCamel = SqliteUtil.getBoolProperty(Constants.IS_CAMEL);
            String tablePrefix = SqliteUtil.getProperty(Constants.TABLE_PREFIX);
            if (translate) {
                String english = YouDaoTranslate.getEnglish(tableComment);
                if (!StringUtils.isBlank(english)) {
                    info.setEntyname(FieldUtil.INSTANCE.englishToField(english, true));
                } else {
                    info.setEntyname(getTablesNameToClassName(tableName.toLowerCase(), tablePrefix));
                }
            } else if (isCamel) {
                info.setEntyname(getTablesNameToClassName(tableName.toLowerCase(), tablePrefix));
            }
            info.setTablename(tableName);
        }
        if (ObjectHelper.isNullOrEmptyString(info.getTablecomment())) {//如果缓存中没有注释
            info.setTablecomment(tableComment);
        }
        DbUtils.saveOrUpdateTableInfo(info, Constants.getSqliteConnection());
        return info;
    }

    public String getTablesNameToClassName(String tableName, String tablePrefix) {
        if (tableName.startsWith(tablePrefix) || tableName.startsWith(tablePrefix.toUpperCase()) || tableName.startsWith(tablePrefix.toLowerCase())) {
            tableName = tableName.substring(tablePrefix.length());
        }

        String[] split = tableName.split("_");
        if (split.length > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                String tempTableName = split[i].substring(0, 1).toUpperCase()
                        + split[i].substring(1, split[i].length());
                sb.append(tempTableName);
            }

            return sb.toString();
        }
        String tempTables = split[0].substring(0, 1).toUpperCase() + split[0].substring(1, split[0].length());
        return tempTables;
    }
}
