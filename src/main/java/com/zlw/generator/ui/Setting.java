package com.zlw.generator.ui;


import com.zlw.generator.plugin.ColumnModuleCellEditor;
import com.zlw.generator.plugin.CustomModuleListModel;
import com.zlw.generator.db.ModuleBean;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/6/30
 *
 */
public class Setting extends JFrame {

    private final MainFrame mainFrame;

    private JScrollPane jScrollPane1;
    private CustomModuleListModel model;
    private JTable jTable;

    private JButton previous;


    public Setting(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        Toolkit tk = Toolkit.getDefaultToolkit();
        setLocation((tk.getScreenSize().width - getSize().width) / 2, (tk.getScreenSize().height - getSize().height) / 2);
        setTitle("设置");
    }

    private void initComponents() {
        jTable = new JTable();
        model = new CustomModuleListModel(this);
        jTable.setDefaultEditor(String.class, new ColumnModuleCellEditor(new JTextField(),this));
        jTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        jScrollPane1 = new JScrollPane();

        jTable.setModel(model);
        jTable.setRowSelectionAllowed(true);
        jTable.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(jTable);
        jTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        model.addDataList();

//        hideColumn(jTable,4);
        TableColumnModel tcm = jTable.getColumnModel();
        TableColumn tc = tcm.getColumn(5);//ID排在数组的第3位
        jTable.removeColumn(tc);//隐藏某列
//        jTable.setDefaultRenderer(Object.class, r);
        jTable.setRowHeight(30);


        previous = new JButton();
        previous.setText("返回");
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upActionPerformed(e);
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        BorderLayout layout = new BorderLayout(3, 3);
        Container container = getContentPane();
        container.setLayout(layout);



        container.add(jScrollPane1, BorderLayout.CENTER);

        JPanel panel = createButtonPanel();
        container.add(panel, BorderLayout.NORTH);

        pack();
    }


    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 4, 30, 5);
        panel.setLayout(gridLayout);

        JButton jButtonAdd = new JButton("增加");
        jButtonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addRow(new ModuleBean());
            }
        });
        panel.add(jButtonAdd);

        JButton jButtonDelete = new JButton("删除");
        jButtonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rows[] = jTable.getSelectedRows();
                model.deleteRow(rows);
            }
        });
        panel.add(jButtonDelete);


        JButton jButtonEdit = new JButton("编辑");
        jButtonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editActionPerformed(e);
            }
        });
        panel.add(jButtonEdit);

        panel.add(new JLabel(""));
        panel.add(previous);
        return panel;
    }

    private void upActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
        mainFrame.setVisible(true);
        mainFrame.refresh();
    }

    private void editActionPerformed(java.awt.event.ActionEvent evt) {
        int rows[] = jTable.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "请选择一行");
            return;
        }
        if (rows.length > 1) {
            JOptionPane.showMessageDialog(this, "只能选择一行");
            return;
        }
        ModuleBean moduleBean=model.getSelected(rows[0]);
        JNotePad notePad = new JNotePad(this,moduleBean,new FileTextDAO());
        notePad.setVisible(true);
        this.setVisible(false);
    }

//    protected void hideColumn(JTable table,int index){
//
//        TableColumn tc= table.getModel().getColumn(index);
//        tc.setMaxWidth(0);
//        tc.setPreferredWidth(0);
//        tc.setMinWidth(0);
//        tc.setWidth(0);
//
//        table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
//        table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
//    }

}
