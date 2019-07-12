package com.zlw.generator.plugin;

import com.zlw.generator.ui.Setting;
import com.zlw.generator.db.Constants;
import com.zlw.generator.db.ModuleBean;
import com.zlw.generator.db.util.DbUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class ColumnModuleCellEditor extends DefaultCellEditor {
    private String oldValue;
    private String value;
    private final JTextField label;
    private int row;
    private int column;
    private CustomModuleListModel model;
    private Setting setting;

    public ColumnModuleCellEditor(JTextField textField, Setting setting) {
        super(textField);
        label = textField;
        this.setting = setting;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value = value == null ? null : value.toString();
        oldValue = this.value;
        this.row = row;
        this.column = column;
        label.setText(this.value);
        model = (CustomModuleListModel) table.getModel();
        return label;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        model.setValueAt(label.getText(), row, column);
        try {
            if (label.getText() != null && StringUtils.isNotBlank(label.getText())) {
                saveModuleInfo();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ColumnModuleCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public void cancelCellEditing() {
        model.setValueAt(oldValue, row, column);
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
//        super.addCellEditorListener(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
//        super.removeCellEditorListener(l);
    }

    private void saveModuleInfo() throws SQLException {
        Object moduleNameObj = model.getValueAt(row, 1);
        if (moduleNameObj == null) {
            return;
        }
        String moduleName = moduleNameObj.toString();
        if (StringUtils.isBlank(moduleName)) {
            return;
        }
//        java.util.List<ModuleBean> modules = new ArrayList<>();
//        modules.addAll(Constants.DEFAULT_MODULES);
//        try {
//            List<ModuleBean> list = DbUtils.getModuleInfo(Constants.getSqliteConnection());
//            modules.addAll(list);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        for (ModuleBean has : modules) {
//            if (has.getModuleName().equalsIgnoreCase(moduleName)) {
//                JOptionPane.showMessageDialog(setting, "名字重复");
//                return;
//            }
//        }

        String fileExt = model.getValueAt(row, 2) == null ? "" : model.getValueAt(row, 2).toString();
        String templateFile = model.getValueAt(row, 3) == null ? "" : model.getValueAt(row, 3).toString();
        String isEdit = model.getValueAt(row, 4) == null ? "" : model.getValueAt(row, 4).toString();
        String serial = model.getValueAt(row, 5) == null ? "" : model.getValueAt(row, 5).toString();
        ModuleBean moduleBean = new ModuleBean();
        moduleBean.setModuleName(moduleName);
        moduleBean.setFileExt(fileExt);
        moduleBean.setTemplateFile(templateFile);
        moduleBean.setIsEdit(true);
        moduleBean.setSerial(serial);
        DbUtils.saveOrUpdateModuleInfo(moduleBean, Constants.getSqliteConnection());
        oldValue = value;
    }
}
