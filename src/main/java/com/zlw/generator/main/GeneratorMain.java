package com.zlw.generator.main;

import com.zlw.generator.plugin.SettingHelper;
import com.zlw.generator.ui.BasicSetting;
import com.zlw.generator.ui.MainFrame;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zhangliewei on 2016/6/16.
 */
public class GeneratorMain {

    /**
     * UIManager中UI字体相关的key
     */
    public static String[] DEFAULT_FONT = new String[]{"Table.font", "TableHeader.font", "CheckBox.font",
            "Tree.font", "Viewport.font", "ProgressBar.font", "RadioButtonMenuItem.font", "ToolBar.font",
            "ColorChooser.font", "ToggleButton.font", "Panel.font", "TextArea.font", "Menu.font", "TableHeader.font",
            "TextField.font", "OptionPane.font", "MenuBar.font", "Button.font", "Label.font", "PasswordField.font",
            "ScrollPane.font", "MenuItem.font", "ToolTip.font", "List.font", "EditorPane.font", "Table.font",
            "TabbedPane.font", "RadioButton.font", "CheckBoxMenuItem.font", "TextPane.font", "PopupMenu.font",
            "TitledBorder.font", "ComboBox.font"};


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
                    UIManager.put("RootPane.setupButtonVisible", false);
                    //设置本属性将改变窗口边框样式定义
                    BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
                    org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
                    // 调整默认字体
                    for (int i = 0; i < DEFAULT_FONT.length; i++) {
                        UIManager.put(DEFAULT_FONT[i], new Font("微软雅黑", Font.PLAIN, 13));
                    }
                    if (SettingHelper.checkDataDir()) {
                        MainFrame codeMainFrame = new MainFrame();
                        codeMainFrame.setVisible(true);
                    } else {
                        BasicSetting basicSetting = new BasicSetting();
                        basicSetting.setVisible(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
