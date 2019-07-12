package com.zlw.generator.plugin;

import com.zlw.generator.ui.Setting;
import com.zlw.generator.db.Constants;
import com.zlw.generator.db.ModuleBean;
import com.zlw.generator.db.util.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class CustomModuleListModel extends DefaultTableModel {

    // 常量配置区

    private final String[] header = {"选择", "模块名", "文件后缀名", "模板名字", "备注", "ID"};
    private final boolean[] isEditAble = {true, true, true, true, false, false};


    private Setting setting;

    public CustomModuleListModel(Setting setting) {
        super();
        this.setting = setting;
    }

    /**
     * 默认情况下这个方法不用重新实现的，但是这样就会造成如果这个列式boolean的类型，就当做string来处理了
     * 如果是boolean的类型那么用checkbox来显示
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
//        return  isEditAble[column];
        List<ModuleBean> modules = Constants.DEFAULT_MODULES;
        if (column == 0) {
            return true;
        }
        if (row < modules.size()) {
            return false;
        } else {
            if (column == isEditAble.length - 1) {
                return false;
            }
            return true;
        }
    }


    public synchronized void addDataList() {
        this.setColumnIdentifiers(header);
        this.getDataVector().clear();
        List<ModuleBean> modules = Constants.DEFAULT_MODULES;
        for (ModuleBean module : modules) {
            Object[] row = new Object[header.length];
            row[0] = false;
            row[1] = module.getModuleName();
            row[2] = module.getFileExt();
            row[3] = module.getTemplateFile();
            row[4] = "系统默认";
            row[5] = module.getSerial();
            addRow(row);
        }
        try {
            List<ModuleBean> list = DbUtils.getModuleInfo(Constants.getSqliteConnection());
            if (list != null && list.size() > 0) {
                for (ModuleBean module : list) {
                    Object[] row = new Object[header.length];
                    row[0] = false;
                    row[1] = module.getModuleName();
                    row[2] = module.getFileExt();
                    row[3] = module.getTemplateFile();
                    row[4] = "自定义";
                    row[5] = module.getSerial();
                    addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addRow(ModuleBean module) {
        Object[] row = new Object[header.length];
        row[0] = false;
        row[1] = module.getModuleName();
        row[2] = module.getFileExt();
        row[3] = module.getTemplateFile();
        row[4] = "自定义";
        row[5] = UUID.randomUUID().toString().replaceAll("-", "");
        addRow(row);
    }


    public void deleteRow(int rows[]) {
        for (int i = 0; i < rows.length; i++) {
            if ((Boolean) getValueAt(rows[i], 0)) {
                String remark = (String) getValueAt(rows[i], 4);
                Boolean isEdit = false;
                if (!remark.equalsIgnoreCase("系统默认")) {
                    isEdit = true;
                }
                ModuleBean moduleBean = new ModuleBean((String) getValueAt(rows[i], 1), (String) getValueAt(rows[i], 2), (String) getValueAt(rows[i], 3), isEdit);
                if (!moduleBean.getIsEdit()) {
                    JOptionPane.showMessageDialog(setting, "模块[" + moduleBean.getModuleName() + "]系统默认,不能删除！");
                } else {
                    try {
                        DbUtils.deleteModuleInfo(moduleBean, Constants.getSqliteConnection());
                        this.removeRow(rows[i]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(setting, "删除失败！");
                    }
                }

            }
        }
    }

    public List<ModuleBean> getSelectedList(int rows[]) {
        List<ModuleBean> list = new ArrayList();
        for (int i = 0; i < rows.length; i++) {
            if ((Boolean) getValueAt(rows[i], 0)) {
                String remark = (String) getValueAt(rows[i], 4);
                Boolean isEdit = false;
                if (!remark.equalsIgnoreCase("系统默认")) {
                    isEdit = true;
                }
                list.add(new ModuleBean((String) getValueAt(rows[i], 1), (String) getValueAt(rows[i], 2), (String) getValueAt(rows[i], 3), isEdit));
            }
        }
        return list;
    }

    public ModuleBean getSelected(int row) {
        ModuleBean moduleBean = new ModuleBean((String) getValueAt(row, 1), (String) getValueAt(row, 2), (String) getValueAt(row, 3), false);
        return moduleBean;
    }


}
