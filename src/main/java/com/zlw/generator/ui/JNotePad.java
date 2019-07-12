package com.zlw.generator.ui;

import com.zlw.generator.plugin.SettingHelper;
import com.zlw.generator.db.ModuleBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JNotePad extends JFrame {
    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem menuOpen;
    private JMenuItem menuSave;
    private JMenuItem menuSaveAs;
    private JMenuItem menuClose;

    private JMenu editMenu;
    private JMenuItem menuCut;
    private JMenuItem menuCopy;
    private JMenuItem menuPast;
    private JMenuItem menuCancel;

    private JMenu backMenu;
    private JMenuItem menuBack;

    private JTextArea textArea;//输入区域
    private JLabel stateBar;//状态条

    private TextDAO textDAO;//保存
    private JFileChooser fileChooser;//文件选择器
    private JPopupMenu popUpMeue; //鼠标点击Menu事件

    private Setting setting;
    private ModuleBean moduleBean;

    public JNotePad(Setting setting, ModuleBean moduleBean, FileTextDAO fileTextDAO) {
        initComponents();//初始组件外观
        initEventListeners();//初始化组件事件倾听器
        Toolkit tk = Toolkit.getDefaultToolkit();
        setLocation((tk.getScreenSize().width - getSize().width) / 2, (tk.getScreenSize().height - getSize().height) / 2);
        setTitle("模板编辑");
        this.setting = setting;
        this.moduleBean = moduleBean;
        this.textDAO = fileTextDAO;
        openModuleFile();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        setSize(800, 500);
        initMenu();
        initTextArea();
        initStateBar();
        popUpMeue = editMenu.getPopupMenu();
        fileChooser = new JFileChooser();
    }

    /*----------------初始化Menu---------------------*/
    private void initMenu() {
        initFileMenu();
        initEditMenu();
        initBackBar();
        initMenuBar();

    }

    private void initMenuBar() {
        //构造菜单列
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(backMenu);
        //设置菜单列
        setJMenuBar(menuBar);
    }

    private void initBackBar() {
        backMenu = new JMenu("返回");
        menuBack = new JMenuItem("返回");
        backMenu.add(menuBack);
    }


    private void initEditMenu() {
        editMenu = new JMenu("编辑");
        menuCut = new JMenuItem("剪切");
        menuCopy = new JMenuItem("复制");
        menuPast = new JMenuItem("粘贴");
        menuCancel = new JMenuItem("撤销");

        editMenu.add(menuCut);
        editMenu.add(menuCopy);
        editMenu.add(menuPast);
        editMenu.add(menuCancel);
    }

    private void initFileMenu() {
        fileMenu = new JMenu("文件");

        menuOpen = new JMenuItem("打开");
        menuSave = new JMenuItem("保存");
        menuSaveAs = new JMenuItem("另存为");
        menuClose = new JMenuItem("关闭");

        fileMenu.add(menuOpen);
        fileMenu.addSeparator(); //分割线;
        fileMenu.add(menuSave);
        fileMenu.add(menuSaveAs);
        fileMenu.addSeparator(); //分割线;
        fileMenu.add(menuClose);
    }
    /*-----------------------------------------------*/

    //初始化事件监视器
    private void initEventListeners() {
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);设置点X关闭
        initAccelerator();
        //下面要设置,点X提示"文档已改变,是否保存"

        //按下窗口关闭按钮事件处理:
        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent event) {
                        closeWindow(event);
                    }
                }
        );

        initMenuListener();//初始化菜单的点击事件

        //编辑区键盘事件:
        textArea.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent event) {
                        jtexAreaActionPerformed(event);
                    }
                }
        );

        //编辑区鼠标事件:
        textArea.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {//3:右键
                            popUpMeue.show(editMenu, mouseEvent.getX(), mouseEvent.getY());
                        }
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            popUpMeue.setVisible(false);
                        }
                    }
                }
        );
    }


    private void initMenuListener() {
        menuOpen.addActionListener(this::openFile);
        menuSave.addActionListener(this::saveFile);
        menuSaveAs.addActionListener(this::saveFileAs);
        menuClose.addActionListener(this::closeFile);
        menuCut.addActionListener(this::cut);
        menuPast.addActionListener(this::past);
        menuCopy.addActionListener(this::copy);
        menuBack.addActionListener(event -> {
            this.setVisible(false);
            setting.setVisible(true);
        });
    }

    private void past(ActionEvent event) {
        textArea.paste();
    }

    private void cut(ActionEvent event) {
        textArea.cut();
    }

    private void copy(ActionEvent event) {
        textArea.copy();
    }

//    private void copy(ActionEvent event) {
//        textArea();
//    }


    private void saveFileAs(ActionEvent event) {
        int option = fileChooser.showDialog(null, null);
        if (option == JFileChooser.APPROVE_OPTION) {
            //在标题栏设定文件名
            setTitle(fileChooser.getSelectedFile().toString());
            File file = new File(fileChooser.getSelectedFile().toString());
            if (!file.exists()) {
                textDAO.create(fileChooser.getSelectedFile().toString());
            } else {
                textDAO.save(file.getAbsolutePath(), textArea.getText());
                stateBar.setText("已修改");
            }
        }
    }

    private void saveFile1(ActionEvent event) {
        Path path = Paths.get(getTitle());
        if (Files.notExists(path)) {
            saveFileAs(event);
        } else {
            try {
                saveFile(event);
            } catch (Throwable e) {
                JOptionPane.showMessageDialog(this, e.toString(),
                        "写入失败", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile(ActionEvent event) {
//        Path path = Paths.get(getTitle());
//        if (Files.notExists(path)) {
//            saveFileAs(event);
//        } else {
        try {
            String resource = SettingHelper.getValue("template.dir") + File.separator + this.moduleBean.getTemplateFile();
//            String path = this.getClass().getClassLoader().getResource(resource).getFile();
            textDAO.save(resource, textArea.getText());
            stateBar.setText("已修改");
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, e.toString(),
                    "写入失败", JOptionPane.ERROR_MESSAGE);
        }
//        }
    }

    private void closeFile(ActionEvent event) {
        if (stateBar.getText().equals("未修改")) {
            dispose();//释放窗口资源,关闭程序
        } else {
            int option = JOptionPane.showConfirmDialog(this,
                    "文档已修改,是否保存:",
                    "保存?", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            switch (option) {
                case JOptionPane.YES_OPTION:
                    saveFile(event);
                    break;
                case JOptionPane.NO_OPTION:
                    dispose();
            }
        }
    }


    private void openModuleFile() {
        textArea.setText("");
        stateBar.setText("未修改");
        String text = null;
        try {
//            java.util.List<ModuleBean> list = Constants.DEFAULT_MODULES;

//            String path = this.getClass().getClassLoader().getResource(resource).getFile();
            File file = new File(SettingHelper.getValue("template.dir"));
            if (!file.exists()) {
                file.mkdirs();
            }
            String resource = file.getAbsolutePath() + File.separator + this.moduleBean.getTemplateFile();
            file = new File(resource);
            if (!file.exists()) {
                file.createNewFile();
            }
            text = textDAO.read(file.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString(),
                    "打开模板失败", JOptionPane.ERROR_MESSAGE);
        }
        textArea.setText(text);

    }


    private void openFile(ActionEvent event) {
        if (stateBar.getText().equals("未修改")) {
            showFileDialog();
        } else {
            int option = JOptionPane.showConfirmDialog(
                    this, "已修改,是否保存?", "保存", JOptionPane.WARNING_MESSAGE, 1);
            switch (option) {
                case JOptionPane.YES_OPTION:
                    saveFile();
                    break;
                case JOptionPane.NO_OPTION:
                    showFileDialog();
                    break;
                default:
                    break;
            }
        }

    }

    private void jtexAreaActionPerformed(KeyEvent event) {
        stateBar.setText("已修改");
    }


    //关闭窗口,并提示是否表存
    private void closeWindow(WindowEvent event) {
        closeFile(new ActionEvent(event.getSource(), event.getID(), "windowClosing"));
    }

    //设置快捷键
    private void initAccelerator() {
        menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));

        menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));

        menuSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK, true));

        //设置快捷键,,略略略
        menuCut.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK)
        );
        menuCopy.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK)
        );
        menuPast.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK)
        );
        menuCancel.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK)
        );
    }

    //设置文本区域
    private void initTextArea() {
        textArea = new JTextArea();
        textArea.setFont(new Font("细明体", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        JScrollPane panel = new JScrollPane(textArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(panel, BorderLayout.CENTER);
    }

    //初始化状态栏(最下方)
    private void initStateBar() {
        stateBar = new JLabel("未修改");
        stateBar.setHorizontalAlignment(SwingConstants.LEFT);
        stateBar.setBorder(BorderFactory.createEtchedBorder());
        getContentPane().add(stateBar, BorderLayout.SOUTH);
    }


    //打开
    private void openFile() {
        if (stateBar.getText().equals("未修改")) {
            showFileDialog();
        } else {
            int option = JOptionPane.showConfirmDialog(
                    this, "已修改,是否保存?", "保存", JOptionPane.WARNING_MESSAGE, Integer.parseInt(null)
            );
            switch (option) {
                case JOptionPane.YES_OPTION:
                    saveFile();
                    break;
                case JOptionPane.NO_OPTION:
                    showFileDialog();
                    break;
                default:
                    break;
            }
        }
    }

    //保存
    private void saveFile() {
    }


    //输出问文件信息
    private void showFileDialog() {
        int option = fileChooser.showDialog(null, null);//文档选取对话框

        if (option == JFileChooser.APPROVE_OPTION) {

            try {
                setTitle(fileChooser.getSelectedFile().toString());
                textArea.setText("");
                textArea.setText("未修改");
                String text = textDAO.read(fileChooser.getSelectedFile().toString());
                textArea.setText(text);
            } catch (Throwable e) {
                JOptionPane.showMessageDialog(this, e.toString(), "打开文档失败", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    //测试函数
    public static void main(String[] args) {
        //将建立的JNotePad实例与SetVisible()的动作排入事件队列
        //这玩意应该是一个Runnable接口的实现
//        SwingUtilities.invokeLater(() -> {
//            new JNotePad(new FileTextDAO()).setVisible(true);//true显示,false隐藏
//        });
//        String resource = "template/custom/" + this.moduleBean.getTemplateFile();
//        String path = this.getClass().getClassLoader().getResource(resource).getFile();
//        File file = new File(path);
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//EntityTemplate.ftl
        String path = JNotePad.class.getClassLoader().getResource("template/default").getFile();
        File file = new File(path);
        System.out.println(path);
        System.out.println(file.getAbsolutePath());

        byte[] datas = null;
        try {
            datas = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            Logger.getLogger(FileTextDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
