package com.zlw.generator.plugin;

import com.zlw.generator.db.Constants;
import com.zlw.generator.db.domain.ColumnInfo;
import com.zlw.generator.db.domain.ColumnModel;
import com.zlw.generator.db.domain.TableBean;
import com.zlw.generator.db.util.DbUtils;
import com.zlw.generator.db.util.SqliteUtil;
import com.zlw.generator.util.FieldUtil;
import com.zlw.generator.util.ObjectHelper;
import com.zlw.generator.util.YouDaoTranslate;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class CustomColumnListModel extends DefaultTableModel {

    // 常量配置区

    private final String[] tableHeader = {"表名", "说明", "字段名", "英文字段名", "字段备注"};
    private final boolean[] isEditAble = {false, false, false, true, true};
    private TableBean tableBean;

    /**
     * 默认情况下这个方法不用重新实现的，但是这样就会造成如果这个列式boolean的类型，就当做string来处理了
     * 如果是boolean的类型那么用checkbox来显示
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return isEditAble[column];
    }

    /**
     * 加一个列表
     */
    public synchronized void addTableList(final TableBean bean) {
        this.setColumnIdentifiers(tableHeader);
        this.getDataVector().clear();
        this.tableBean = bean;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (ColumnModel columnModel : bean.getColumnList()) {
                    try {
                        ColumnInfo columnInfo = getColumnInfo(bean.getTableName(), columnModel.getColumnName(), columnModel.getFieldComment());
                        columnModel = columnInfo == null ? columnModel : columnInfo;
                        Object[] row = new Object[tableHeader.length];
                        row[0] = bean.getTableName();
                        row[1] = bean.getTableComment();
                        row[2] = columnModel.getColumnName();
                        row[3] = columnModel.getFieldName();
                        row[4] = columnModel.getFieldComment();
                        addRow(row);
                        ColumnModel get = bean.getColumnList().get(i++);
                        get.setFieldName(columnModel.getFieldName());
                        get.setFieldComment(columnModel.getFieldComment());
                    } catch (SQLException ex) {
//                        SwingLogUtil.showError(ex);
                        Logger.getLogger(CustomColumnListModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        thread.start();
    }

    public ColumnModel getColumnModel(int index) {
        return tableBean.getColumnList().get(index);
    }

    public void reTranslate() throws SQLException {
        DbUtils.clearColumnEnglish(tableBean.getTableName(), Constants.getSqliteConnection());
        addTableList(tableBean);
    }


    public String getStandardName(String columnName) {
        String[] split = columnName.split("_");
        if (split.length > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                String tempTableName = split[i].substring(0, 1).toUpperCase()
                        + split[i].substring(1, split[i].length());
                sb.append(tempTableName);
            }
            String s = sb.toString();
            s = s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
            return s;
        }
        String tempTables = split[0].substring(0, 1).toLowerCase() + split[0].substring(1, split[0].length());
        return tempTables;
    }

    private ColumnInfo getColumnInfo(String tableName, String columnName, String columnComment) throws SQLException {
        ColumnInfo info = DbUtils.getColumnInfo(tableName, columnName, Constants.getSqliteConnection());
        if (info == null || ObjectHelper.isNullOrEmptyString(info.getFieldname())) {
            info = new ColumnInfo();
            String english;
            if (columnName.equalsIgnoreCase("bbh")) {
                english = "version";
            } else {
                Boolean translate = SqliteUtil.getBoolProperty(Constants.IS_TRANSLATE);
                Boolean isCamel = SqliteUtil.getBoolProperty(Constants.IS_CAMEL);
                String comment = info.getColumncomment();
                if (translate) {
                    english = YouDaoTranslate.getEnglish(comment);
                    if (!StringUtils.isBlank(english)) {
                        info.setFieldname(FieldUtil.INSTANCE.englishToField(english, true));
                    } else {
                        info.setFieldname(getStandardName(columnName.toLowerCase()));
                    }
                } else if (isCamel) {
                    info.setFieldName(getStandardName(columnName.toLowerCase()));
                }

//                DicBean dicBean = new DicBean(columnName);
//                DicBean dic = DbUtils.getDic(dicBean,Constants.getSqliteConnection());
//                if (dic == null || ObjectHelper.isNullOrEmptyString(dic.getEnglish())) {
//                    english = YouDaoTranslate.getEnglish(columnComment);
//                } else {
//                    english = dic.getEnglish();
//                }
            }
//            if (!ObjectHelper.isNullOrEmptyString(english)) {
//                info.setFieldname(FieldUtil.INSTANCE.englishToField(english, false));
//            }else{
//                info.setFieldname(FieldUtil.INSTANCE.englishToField(columnName, false));
//            }
            info.setTablename(tableName);
        }
        if (ObjectHelper.isAllEmpty(columnComment, info.getColumncomment())) {
            info.setColumncomment(columnName);
        } else if (ObjectHelper.isAllNotNull(info.getColumncomment(), columnName) && !info.getColumncomment().equals(columnName)) {
            info.setColumncomment(info.getColumncomment());
        } else if (info.getColumncomment() == null && columnComment != null) {
            info.setColumncomment(columnComment);
        } else {
            info.setColumncomment(columnName);
        }
        info.setColumnname(columnName);
        DbUtils.saveOrUpdateColumnInfo(info, Constants.getSqliteConnection());
        return info;
    }
}
