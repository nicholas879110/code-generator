package com.zlw.generator.ui;

import com.zlw.generator.factory.CodeGenerateFactory;
import com.zlw.generator.plugin.ColumnCellEditor;
import com.zlw.generator.plugin.CustomColumnListModel;
import com.zlw.generator.db.domain.TableBean;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class SelectColumns extends JFrame {
    private final List<TableBean> tableBeans;
    private final CustomColumnListModel listModel = new CustomColumnListModel();
    private int index;
    private final SelectTables selectTables;

    public SelectColumns(final SelectTables selectTables, List<TableBean> tableBeans, int index) {
        initComponents();
        countLabel.setText("当前是第" + (index + 1) + "/" + tableBeans.size() + "个表");
//        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jTable1.setDefaultEditor(String.class, new ColumnCellEditor(new JTextField()));
        jTable1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        this.selectTables = selectTables;
        this.tableBeans = tableBeans;
        this.index = index;
        if (index == tableBeans.size() - 1) {
            next.setEnabled(false);
        }
        Toolkit tk = Toolkit.getDefaultToolkit();
        setLocation((tk.getScreenSize().width - getSize().width) / 2,
                (tk.getScreenSize().height - getSize().height) / 2);
        setTitle("调整字段");
        listModel.addTableList(tableBeans.get(index));
//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent event) {
//                dispose();
//                selectTables.setVisible(true);
//            }
//        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        up = new JButton();
        next = new JButton();
        jButton3 = new JButton();
        jButton1 = new JButton();
        countLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        jTable1.setModel(listModel);
        jScrollPane1.setViewportView(jTable1);

        up.setText("上一步");
        up.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upActionPerformed(evt);
            }
        });

        next.setText("下一步");
        next.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });

        jButton3.setText("完成");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setText("重新翻译");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        countLabel.setText("第/个表");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(up)
                                .addGap(36, 36, 36)
                                .addComponent(next)
                                .addGap(76, 76, 76)
                                .addComponent(countLabel)
                                .addGap(74, 74, 74)
                                .addComponent(jButton1)
                                .addGap(46, 46, 46)
                                .addComponent(jButton3)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(up)
                                        .addComponent(next)
                                        .addComponent(jButton3)
                                        .addComponent(jButton1)
                                        .addComponent(countLabel))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upActionPerformed
        this.dispose();
        if (index != 0) {
            new SelectColumns(selectTables, tableBeans, --index).setVisible(true);
        } else {
            selectTables.setVisible(true);
        }
    }//GEN-LAST:event_upActionPerformed

    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
        try {
            this.dispose();
            new SelectColumns(selectTables, tableBeans, ++index).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }//GEN-LAST:event_nextActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            for (TableBean tableBean : tableBeans) {
                CodeGenerateFactory.codeGenerate(tableBean);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
        JOptionPane.showMessageDialog(this, "生成完毕！");
    }

    public static boolean newFolder(String path) {
        return new File(path).mkdirs();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            listModel.reTranslate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }

    private JLabel countLabel;
    private JButton jButton1;
    private JButton jButton3;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JButton next;
    private JButton up;
}
