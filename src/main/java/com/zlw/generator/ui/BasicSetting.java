package com.zlw.generator.ui;

import com.zlw.generator.plugin.SettingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhangliewei on 2016/6/30.
 */
public class BasicSetting extends JFrame {

    private static final Logger log = LoggerFactory.getLogger(BasicSetting.class);

    private JTextField jTextField;
    private JPanel jPanel;
    private JButton jButton;

    private JButton nextButton;

    public BasicSetting() {
        initComponents();
        Toolkit tk = Toolkit.getDefaultToolkit();
        setLocation((tk.getScreenSize().width - getSize().width) / 2, (tk.getScreenSize().height - getSize().height) / 2);
    }

    private void initComponents() {
        jTextField = new JTextField(30);
        jTextField.setText("C:\\Users\\King L Zhang\\AppData\\Local\\CodeGenerator");
        jTextField.setEditable(false);
        jButton = new JButton("浏览");

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionFileSelect(e);
            }
        });

        nextButton = new JButton("下一步");

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionNext(e);
            }
        });

        jPanel = new JPanel();

        jPanel.add(jTextField);
        jPanel.add(jButton);
        jPanel.add(nextButton);


        getContentPane().add(jPanel);

        setTitle("选择安装目录");

        setSize(500, 150);
        pack();
    }

    private void actionNext(ActionEvent e) {
        String path = jTextField.getText();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        SettingHelper.settting(file.getAbsolutePath());
        try {
            SettingHelper.copyResource(file.getAbsolutePath());
        } catch (IOException ex) {
            log.error("copy resource", ex);
            ex.printStackTrace();
            return;
        }

        MainFrame codeMainFrame = new MainFrame();
        codeMainFrame.setVisible(true);
        this.setVisible(false);
    }

    private void actionFileSelect(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.setDialogTitle("选择目录");
        int result = jFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            jTextField.setText(path + File.separator + "CodeGenerator");
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.home"));
    }
}
