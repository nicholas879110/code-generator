package com.zlw.generator.plugin;

import com.zlw.generator.db.Constants;
import com.zlw.generator.db.domain.ColumnInfo;
import com.zlw.generator.db.domain.ColumnModel;
import com.zlw.generator.db.util.DbUtils;

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
 * @Copyright(c)  PwC.普华永道
 */
public class ColumnCellEditor  extends DefaultCellEditor {
    private String oldValue;
    private String value;
    private final JTextField label;
    private int row;
    private int column;
    private CustomColumnListModel model;

    public ColumnCellEditor(JTextField textField) {
        super(textField);
        label =textField;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value =value==null?null: value.toString();
        oldValue =this.value;
        this.row = row;
        this.column = column;
        label.setText(this.value);
        model = (CustomColumnListModel)table.getModel();
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
        if (label.getText()!=null&&oldValue!=null&&!oldValue.trim().equals(label.getText())) {
            try {
                saveTableInfo();
            } catch (SQLException ex) {
                Logger.getLogger(ColumnCellEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    @Override
    public void cancelCellEditing() {
        model.setValueAt(oldValue, row, column);
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
    }

    private void saveTableInfo() throws SQLException {
        String tableName=model.getValueAt(row, 0).toString();
        String columnName = model.getValueAt(row, 2).toString();
        String fieldName = model.getValueAt(row, 3).toString();
        String columnComment = model.getValueAt(row, 4).toString();
        ColumnModel columnModel = model.getColumnModel(row);
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setTablename(tableName);
        columnInfo.setColumnname(columnName);
        columnInfo.setFieldname(fieldName);
        columnInfo.setColumncomment(columnComment);
        columnModel.setFieldName(fieldName);
        columnModel.setFieldComment(columnComment);
        DbUtils.saveOrUpdateColumnInfo(columnInfo, Constants.getSqliteConnection());
        oldValue=value;
    }
}
