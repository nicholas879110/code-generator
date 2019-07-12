package com.zlw.generator.ui;

import com.zlw.generator.db.Constants;
import com.zlw.generator.db.ModuleBean;
import com.zlw.generator.db.MysqlDatabaseInfo;
import com.zlw.generator.db.OracleDatabaseInfo;
import com.zlw.generator.db.util.DbUtils;
import com.zlw.generator.db.util.SqliteUtil;
import com.zlw.generator.util.ObjectHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangliewei on 2016/6/30.
 */
public class MainFrame extends JFrame {

    JMenuBar menuBar;


    private JLabel jdbcUrlJl;
    private JTextField jdbcUrlJt;

    private JLabel jdbcUsernameJl;
    private JTextField jdbcUsernameJt;

    private JLabel jdbcPasswordJl;
    private JPasswordField jdbcPasswordJt;

    private JLabel generateTitle;

    private JLabel projectPathJl;
    private JTextField projectPathJt;

    private JLabel packageNameJl;
    private JTextField packageNameJt;

    private JLabel projectVersionJl;
    private JTextField projectVersionJt;

    private JLabel projectAuthorJl;
    private JTextField projectAuthorJt;

    private java.util.List<JCheckBox> bizsJc;
    private java.util.List<JTextField> bizsJt;

    private ButtonGroup buttonGroup;
    private JRadioButton isTranslate;
    private JRadioButton isCamel;
    private JTextField tablePrefix;

    private JPanel modulesPanel;

    private JButton jButtonNext;


    public MainFrame() {
        initComponents();
        if (ObjectHelper.isNullOrEmptyString(SqliteUtil.getProperty("version"))) {
            SqliteUtil.setProperty("version", "1.0.0-SNAPSHOT");
        }
        Toolkit tk = Toolkit.getDefaultToolkit();
        setLocation((tk.getScreenSize().width - getSize().width) / 2, (tk.getScreenSize().height - getSize().height) / 2);
        SqliteUtil.bindAllField(this);
        setResizable(false);
    }

    private void initComponents() {
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu menuSetting = new JMenu("设置(S)");
        menuSetting.setMnemonic('S');
        menuBar.add(menuSetting);

        menuSetting.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                settingActionPerformed(e);
//                JOptionPane.showMessageDialog(rootPane, "select");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
//                JOptionPane.showMessageDialog(rootPane, "deselect");
            }

            @Override
            public void menuCanceled(MenuEvent e) {
//                JOptionPane.showMessageDialog(rootPane, "cancel");
            }
        });

//        JMenu itemOpen = new JMenu("编辑模板(E)");
//        itemOpen.setMnemonic('E');
//        JMenuItem itemOpen1 = new JMenuItem("打开x");
//        JMenuItem itemOpen2 = new JMenuItem("打开y");
//        itemOpen.add(itemOpen1);
//        itemOpen.add(itemOpen2);
//        JMenuItem itemEditModule = new JMenuItem("编辑模板");
//        itemOpen.setMnemonic('E');
//        menuFile.add(itemSave);

        jdbcUrlJl = new JLabel();
        jdbcUrlJl.setText("数据库路径:");
        jdbcUrlJt = new JTextField();
        jdbcUrlJt.setName(Constants.JDBC_URL);

        jdbcUsernameJl = new JLabel();
        jdbcUsernameJl.setText("用户名：");
        jdbcUsernameJt = new JTextField(15);
        jdbcUsernameJt.setName(Constants.JDBC_USERNAME);

        jdbcPasswordJl = new JLabel();
        jdbcPasswordJl.setText("密码：");
        jdbcPasswordJt = new JPasswordField(15);
        jdbcPasswordJt.setName(Constants.JDBC_PASSWORD);

        generateTitle = new JLabel("生成信息配置");


        projectPathJl = new JLabel();
        projectPathJl.setText("工程路径：");
        projectPathJt = new JTextField();
        projectPathJt.setName(Constants.PROJECT_PATH);

        packageNameJl = new JLabel();
        packageNameJl.setText("工程包名：");
        packageNameJt = new JTextField();
        packageNameJt.setName(Constants.PACKAGE_NAME);

        projectVersionJl = new JLabel();
        projectVersionJl.setText("工程版本:");
        projectVersionJt = new JTextField();
        projectVersionJt.setName(Constants.PROJECT_VERSION);

        projectAuthorJl = new JLabel();
        projectAuthorJl.setText("作者:");
        projectAuthorJt = new JTextField();
        projectAuthorJt.setName(Constants.PROJECT_AUTHOR);

        buttonGroup = new ButtonGroup();
        isTranslate = new JRadioButton();
        isTranslate.setText("通过翻译注释");
        isTranslate.setName(Constants.IS_TRANSLATE);
        isCamel = new JRadioButton();
        isCamel.setText("数据库原始字段(如需去掉表明前缀，请填入)");
        isCamel.setName(Constants.IS_CAMEL);
        buttonGroup.add(isTranslate);
        buttonGroup.add(isCamel);
        tablePrefix = new JTextField();
        tablePrefix.setName(Constants.TABLE_PREFIX);

        jButtonNext = new JButton();
        jButtonNext.setText("下一步");
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        modulesPanel = createInfoPanel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Container container = getContentPane();

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(5, 10, 5, 10);
        container.setLayout(gridBagLayout);

        addComponent(container, gridBagLayout, gridBagConstraints, jdbcUrlJl, 0, 0, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, jdbcUrlJt, 1, 0, 3, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, jdbcUsernameJl, 0, 1, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, jdbcUsernameJt, 1, 1, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, jdbcPasswordJl, 2, 1, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, jdbcPasswordJt, 3, 1, 1, 1, 0, 0);
        //addComponent(container,gridBagLayout,gridBagConstraints,generateTitle,0,2,4,1,0,0);
        addComponent(container, gridBagLayout, gridBagConstraints, projectPathJl, 0, 3, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, projectPathJt, 1, 3, 3, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, packageNameJl, 0, 4, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, packageNameJt, 1, 4, 3, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, projectVersionJl, 0, 5, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, projectVersionJt, 1, 5, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, projectAuthorJl, 2, 5, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, projectAuthorJt, 3, 5, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, modulesPanel, 0, 6, 4, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, isCamel, 0, 7, 3, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, tablePrefix, 3, 7, 1, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, isTranslate, 0, 8, 2, 1, 0, 0);
        addComponent(container, gridBagLayout, gridBagConstraints, jButtonNext, 3, 9, 1, 1, 0, 0);
        pack();
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        TitledBorder border = BorderFactory.createTitledBorder("生成信息");
        border.setTitleColor(Color.BLACK);
        panel.setBorder(border);

        addModulesComponent(panel);

        return panel;
    }

    private void addModulesComponent(JPanel panel) {
        List<ModuleBean> modules = new ArrayList<>();
        modules.addAll(Constants.DEFAULT_MODULES);
        try {
            List<ModuleBean> list = DbUtils.getModuleInfo(Constants.getSqliteConnection());
            modules.addAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        GridLayout gridLayout = new GridLayout(modules.size() / 2 == 0 ? modules.size() / 2 + 1 : modules.size() / 2 + 2, 4, 30, 5);
        panel.setLayout(gridLayout);
        JLabel Jbl1 = new JLabel("是否生成");
        JLabel Jbl2 = new JLabel("模块");
        panel.add(Jbl1);
        panel.add(Jbl2);
        JLabel Jbl3 = new JLabel("是否生成");
        JLabel Jbl4 = new JLabel("模块");
        panel.add(Jbl3);
        panel.add(Jbl4);
        bizsJt = new ArrayList<>();
        bizsJc = new ArrayList<>();
        for (ModuleBean module : modules) {
            JCheckBox jcb = new JCheckBox(module.getModuleName());
            jcb.setName(Constants.MODULE_IS_GENERATE + module.getModuleName());
            jcb.setSelected(true);
            JTextField jtf = new JTextField();
            jtf.setName(Constants.MODULE_PREFIX + module.getModuleName());
            panel.add(jcb);
            panel.add(jtf);
            bizsJc.add(jcb);
            bizsJt.add(jtf);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            SelectTables selectTables = new SelectTables(this);
            if (SqliteUtil.getProperty("jdbc_url").contains("mysql")) {
                selectTables.setDatabaseInfo(new MysqlDatabaseInfo(Constants.fieldmapping));
            } else {
                selectTables.setDatabaseInfo(new OracleDatabaseInfo(Constants.fieldmapping));
            }
            this.setVisible(false);
            selectTables.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }

    private void settingActionPerformed(MenuEvent evt) {
        try {
//            SelectTables selectTables = new SelectTables(this);
//            if (SqliteUtil.getProperty("jdbc_url").contains("mysql")) {
//                selectTables.setDatabaseInfo(new MysqlDatabaseInfo(Constants.fieldmapping));
//            } else {
//                selectTables.setDatabaseInfo(new OracleDatabaseInfo(Constants.fieldmapping));
//            }
//            this.setVisible(false);
//            selectTables.setVisible(true);

            Setting setting = new Setting(this);
            this.setVisible(false);
            setting.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }


    protected void addComponent(
            Container container, GridBagLayout layout,
            GridBagConstraints constraints,
            Component componentToAdd, int gridx, int gridy, int gridwidth,
            int gridheight, int weightx, int weighty) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        layout.setConstraints(componentToAdd, constraints);
        container.add(componentToAdd);
    }


    public void refresh() {
        modulesPanel.removeAll();
        addModulesComponent(modulesPanel);
        modulesPanel.updateUI();
        modulesPanel.repaint();
        SqliteUtil.bindAllField(this);
    }
}
