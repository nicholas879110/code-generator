package com.zlw.generator.db.util;

import com.zlw.generator.plugin.SettingHelper;
import com.zlw.generator.db.config.DbConfig;
import com.zlw.generator.db.config.SimpleConfigurator;
import com.zlw.generator.db.crud.DataBaseManager;
import com.zlw.generator.db.crud.DatabaseAccess;
import com.zlw.generator.db.transaction.IDbManager;
import com.zlw.generator.db.transaction.SingleManager;
import com.zlw.generator.util.NumberUtils;
import com.zlw.generator.util.ObjectHelper;
import com.zlw.generator.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class SqliteUtil {

    private static final Logger log = LoggerFactory.getLogger(SqliteUtil.class);
    private static final Map<String, String> PropertiesMap = Collections.synchronizedMap(new HashMap());

    public SqliteUtil() {
    }

    public static IDbManager getManager(final boolean... commit) {
        if (SimpleConfigurator.INSTANCE.getDbConfig("sqlite") == null) {
            SimpleConfigurator.addConfigurator(new DbConfig("org.sqlite.JDBC", "jdbc:sqlite:"+ SettingHelper.getValue("sqlliteUrl.url"), "", "", "sqlite"));
            if (!tableExist("properties")) {
                createParmeterTable();
            }
        }
        SingleManager manager = new SingleManager("sqlite") {
            @Override
            public synchronized void commit() throws SQLException {
                if (commit.length <= 0 || commit[0]) {
                    super.commit();
                }
            }
        };
        return manager;
    }

    public static boolean tableExist(String tableName) {
        DatabaseAccess lite = new DatabaseAccess(getManager(new boolean[0]));

        try {
            Object ex = lite.queryUnique("SELECT COUNT(*) FROM sqlite_master where type=\'table\' and name=\'" + tableName + "\'");
            return Integer.valueOf(1).equals(ex);
        } catch (SQLException var3) {
            log.error("check table exist error tablename:{}", var3, tableName);
            return false;
        }
    }

    public static void createParmeterTable() {
        String sql = "create table properties(key text,value text);";
        execute(sql);
    }

    public static void execute(String sql) {
        DatabaseAccess lite = new DatabaseAccess(getManager(new boolean[0]));

        try {
            lite.update(sql);
        } catch (SQLException var3) {
            log.error("create table error", var3);
        }

    }

    public static String getProperty(String key) {
        String pvalue = (String) PropertiesMap.get(key);
        if (pvalue == null) {
            String sql = "select value from properties where key =\'" + key + "\'";
            DatabaseAccess lite = new DatabaseAccess(getManager(new boolean[]{false}));

            try {
                Object ex = lite.queryUnique(sql);
                pvalue = ex == null ? "" : ex.toString();
            } catch (SQLException var5) {
                log.error("getProperty error key:{}", var5, key);
            }
        }

        PropertiesMap.put(key, pvalue);
        return pvalue;
    }

    public static boolean getBoolProperty(String key) {
        String pvalue = getProperty(key);
        return pvalue == null ? false : "true".equalsIgnoreCase(pvalue);
    }

    public static Long getLongProperty(String key) {
        String pvalue = getProperty(key);
        return Long.valueOf(pvalue == null ? 0L : NumberUtils.parseDouble(pvalue).longValue());
    }

    public static void setProperty(String key, String value, boolean... commit) {
        if (!ObjectHelper.isNullOrEmptyString(key)) {
            if (ObjectHelper.isNullOrEmptyString(value)) {
                value = "";
            }

            String pvalue = getProperty(key);
            if (!value.equals(pvalue)) {
                Properties property = new Properties(key, value);
                DataBaseManager hyberbin = new DataBaseManager(property, getManager(commit));

                try {
                    hyberbin.deleteByKey("key");
                    hyberbin = new DataBaseManager(property, getManager(commit));
                    hyberbin.insert("");
                    PropertiesMap.put(key, value);
                } catch (SQLException var7) {
                    log.error("setProperty key:{},value:{} error!", new Object[]{var7, key, value});
                }

            }
        }
    }

    public static void clearProperties() {
        DatabaseAccess databaseAccess = new DatabaseAccess(getManager(new boolean[0]));
        try {
            databaseAccess.update("delete from Properties");
            PropertiesMap.clear();
        } catch (SQLException var2) {
            log.error("clearProperties error!", var2);
        }

    }

    public static void bindJTextField(final JTextField field) {
        String name = field.getName();
        field.setText(getProperty(name));
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                SqliteUtil.setProperty(name, field.getText(), new boolean[0]);
            }
        });
        if (field instanceof JPasswordField) {
            field.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    ((JPasswordField) field).setEchoChar('\u0000');
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    ((JPasswordField) field).setEchoChar('*');
                }
            });
        }

    }

    public static void bindJRadioField(final JRadioButton field) {
        final String name = field.getName();
        field.setSelected(getBoolProperty(name));
        field.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SqliteUtil.setProperty(name, field.isSelected() + "", new boolean[0]);
            }
        });
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                SqliteUtil.setProperty(name, field.isSelected() + "", new boolean[0]);
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (!field.isSelected()) {
                    SqliteUtil.setProperty(name, "true", new boolean[0]);
                }

            }
        });
    }

    public static void bindJCheckBoxField(final JCheckBox field) {
        final String name = field.getName();
        field.setSelected(getBoolProperty(name));
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                SqliteUtil.setProperty(name, field.isSelected() + "", new boolean[0]);
            }
        });
    }

    public static void bindJComboxField(final JComboBox field) {
        String name = field.getName();
        field.setSelectedItem(getProperty(name));
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                SqliteUtil.setProperty(name, field.getSelectedItem().toString(), new boolean[0]);
            }
        });
    }

    public static void bindAllField(JFrame frame) {
        Field[] fields = frame.getClass().getDeclaredFields();
        int length = fields.length;
        for (int i = 0; i < length; ++i) {
            Field field = fields[i];
            bindField(frame, field);
        }

    }

    private static void bindField(JFrame frame, Field field) {
        if (JTextField.class.isAssignableFrom(field.getType())) {
            try {
                field.setAccessible(true);
                bindJTextField((JTextField) field.get(frame));
            } catch (Exception var7) {
                log.error("绑定事件错误", var7);
            }
        } else if (JRadioButton.class.isAssignableFrom(field.getType())) {
            try {
                field.setAccessible(true);
                bindJRadioField((JRadioButton) field.get(frame));
            } catch (Exception var10) {
                log.error("绑定事件错误", var10);
            }
        } else if (JCheckBox.class.isAssignableFrom(field.getType())) {
            try {
                field.setAccessible(true);
                bindJCheckBoxField((JCheckBox) field.get(frame));
            } catch (Exception var9) {
                log.error("绑定事件错误", var9);
            }
        } else if (JComboBox.class.isAssignableFrom(field.getType())) {
            try {
                field.setAccessible(true);
                bindJComboxField((JComboBox) field.get(frame));
            } catch (Exception var8) {
                log.error("绑定事件错误", var8);
            }
        } else if (List.class.isAssignableFrom(field.getType())) {
            try {
                field.setAccessible(true);
                ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
                Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
                Class clz = (Class) listActualTypeArguments[0];//得到对象list中实例的类型
                Class clazz = field.get(frame).getClass();//获取到属性的值的Class对象
                Method m = clazz.getDeclaredMethod("size");
                int size = (Integer) m.invoke(field.get(frame));//调用list的size方法，得到list的长度
                for (int i = 0; i < size; i++) {//遍历list，调用get方法，获取list中的对象实例
                    Method getM = clazz.getDeclaredMethod("get", int.class);
                    if (!getM.isAccessible()) {
                        getM.setAccessible(true);
                    }
                    Object obj = getM.invoke(field.get(frame), i);

                    if (JTextField.class.isAssignableFrom(clz)) {
                        try {
                            field.setAccessible(true);
                            bindJTextField((JTextField) obj);
                        } catch (Exception var7) {
                            log.error("绑定事件错误", var7);
                        }
                    } else if (JRadioButton.class.isAssignableFrom(clz)) {
                        try {
                            field.setAccessible(true);
                            bindJRadioField((JRadioButton) obj);
                        } catch (Exception var10) {
                            log.error("绑定事件错误", var10);
                        }
                    } else if (JCheckBox.class.isAssignableFrom(clz)) {
                        try {
                            field.setAccessible(true);
                            bindJCheckBoxField((JCheckBox) obj);
                        } catch (Exception var9) {
                            log.error("绑定事件错误", var9);
                        }
                    } else if (JComboBox.class.isAssignableFrom(clz)) {
                        try {
                            field.setAccessible(true);
                            bindJComboxField((JComboBox) obj);
                        } catch (Exception var8) {
                            log.error("绑定事件错误", var8);
                        }
                    }
                }
            } catch (Exception var8) {
                log.error("绑定事件错误", var8);
            }
        }
    }

    static {


    }
}
