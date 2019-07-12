package com.zlw.generator.ui;


import com.zlw.generator.factory.CodeGenerateFactory;
import com.zlw.generator.plugin.CustomCellEditor;
import com.zlw.generator.plugin.CustomTableListModel;
import com.zlw.generator.db.ADatabaseInfo;
import com.zlw.generator.db.domain.TableBean;
import com.zlw.generator.db.domain.TableInfo;
import com.zlw.generator.util.ObjectHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/6/30
 *
 */
public class SelectTables extends JFrame {

    private ADatabaseInfo databaseInfo;
    private final CustomTableListModel model;
    private final MainFrame mainFrame;

    /**
     * Creates new form ReverseFrame
     */
    public SelectTables(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        model = new CustomTableListModel();
        initComponents();
        jTable1.setDefaultEditor(String.class, new CustomCellEditor(new JTextField()));
        jTable1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        Toolkit tk = Toolkit.getDefaultToolkit();
        setLocation((tk.getScreenSize().width - getSize().width) / 2,
                (tk.getScreenSize().height - getSize().height) / 2);
        setTitle("选择表");
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public void setDatabaseInfo(ADatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
        model.addTableList(databaseInfo.getTableList());
    }

    private void initComponents() {

        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        next = new JButton();
        jButton1 = new JButton();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jButton2 = new JButton();
        jLabel3 = new JLabel();
        search = new JTextField();
        jButton5 = new JButton();
        jButton3 = new JButton();
        jButton6 = new JButton();
        previous = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        jLabel1.setText("第二步：选择表");

        jLabel2.setText("请选择你要逆向的表：");

        next.setText("下一步");
        next.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });

        jButton1.setText("反选");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(model);
        jTable1.setRowSelectionAllowed(true);
        jTable1.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        jButton2.setText("全选");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("搜索:");

        jButton5.setText("确定");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setText("重译选中");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setText("完成");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        previous.setText("上一步");
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upActionPerformed(e);
            }
        });


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE))
                                                .addGap(8, 8, 8))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel3)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(search)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(jButton5))))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(55, 55, 55)
                                                .addComponent(previous)
                                                .addGap(44, 44, 44)
                                                .addComponent(jButton2)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton1)
                                                .addGap(44, 44, 44)
                                                .addComponent(jButton3)
                                                .addGap(39, 39, 39)
                                                .addComponent(next)
                                                .addGap(34, 34, 34)
                                                .addComponent(jButton6)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(search, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton5))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGap(8, 8, 8)
                                .addComponent(jLabel2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(previous)
                                        .addComponent(jButton2)
                                        .addComponent(jButton1)
                                        .addComponent(next)
                                        .addComponent(jButton3)
                                        .addComponent(jButton6))
                                .addGap(22, 22, 22))
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        model.selectAll();
    }

    private void nextActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            java.util.List<TableInfo> selectedList = model.getSelectedList();
            if (ObjectHelper.isNotEmpty(selectedList)) {
                java.util.List<TableBean> tableBeans = new ArrayList<TableBean>();
                for (TableInfo map : selectedList) {
                    TableBean tableBean = databaseInfo.getTableInfo(map);
                    tableBeans.add(tableBean);
                }
                this.dispose();
                SelectColumns selectColumns = new SelectColumns(this, tableBeans, 0);
                selectColumns.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }

    private void buildModel() {
        java.util.List<TableInfo> selectedList = model.getSelectedList();


        for (TableInfo map : selectedList) {
            TableBean tableBean = databaseInfo.getTableInfo(map);
            CodeGenerateFactory.codeGenerate(tableBean);
        }
        JOptionPane.showMessageDialog(this, "生成完毕！");
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        model.selectAllTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            model.addTableList(databaseInfo.getTableList(search.getText()));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            model.reTranslate(databaseInfo.getTableList(search.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            buildModel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upActionPerformed
        this.dispose();
        mainFrame.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton5;
    private JButton jButton6;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    public JTable jTable1;
    private JButton next;
    private JTextField search;
    private JButton previous;
    // End of variables declaration//GEN-END:variables

}
