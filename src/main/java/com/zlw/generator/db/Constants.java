package com.zlw.generator.db;

import com.zlw.generator.db.config.ConfigCenter;
import com.zlw.generator.db.config.DbConfig;
import com.zlw.generator.db.config.SimpleConfigurator;
import com.zlw.generator.db.transaction.IDbManager;
import com.zlw.generator.db.transaction.SingleManager;
import com.zlw.generator.db.util.SimpleSqlout;
import com.zlw.generator.db.util.SqliteUtil;
import com.zlw.generator.util.LoadProperties;
import com.zlw.generator.util.ObjectHelper;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class Constants {

    public static final String JDBC_URL = "jdbc_url";
    public static final String JDBC_USERNAME = "jdbc_username";
    public static final String JDBC_PASSWORD = "jdbc_password";

    public static final String PROJECT_PATH = "project_path";
    public static final String PACKAGE_NAME = "package_name";
    public static final String PROJECT_VERSION = "project_version";
    public static final String PROJECT_AUTHOR = "project_author";
    public static final String MODULE_PREFIX = "module_";
    public static final String MODULE_IS_GENERATE = "is_module_";
    public static final String IS_TRANSLATE = "is_translate";
    public static final String IS_CAMEL = "is_camel";
    public static final String TABLE_PREFIX = "table_prefix";


    public static final java.util.List<ModuleBean> DEFAULT_MODULES = new ArrayList<ModuleBean>() {{
        add(new ModuleBean("entity", ".java", "EntityTemplate.ftl", true));
        add(new ModuleBean("mapper", ".xml", "MapperTemplate.ftl", true));
        add(new ModuleBean("dao", ".java", "DaoTemplate.ftl", true));
        add(new ModuleBean("dao.impl", ".java", "DaoImplTemplate.ftl", true));
        add(new ModuleBean("service", ".java", "ServiceTemplate.ftl", true));
        add(new ModuleBean("service.impl", ".java", "ServiceImplTemplate.ftl", true));
        add(new ModuleBean("facade", ".java", "FacadeTemplate.ftl", true));
        add(new ModuleBean("controller", ".java", "ControllerTemplate.ftl", true));
        add(new ModuleBean("test", ".java", "TestTemplate.ftl", true));
    }};


    public static final int LOAD_SOURCE_SQLITE = 1;
    public static final int LOAD_SOURCE_DB = 2;
    public static final int LOAD_SOURCE_DEFAULT = 0;

    public static LoadProperties fieldmapping = new LoadProperties("fieldmapping.properties");

    public static String getBasePath() {
        return SqliteUtil.getProperty("basePath");
    }

    /**
     * 应用组名
     */
    public static String getGroup() {
        return SqliteUtil.getProperty("group");
    }

    /**
     * 应用名
     */
    public static String getApp() {
        return SqliteUtil.getProperty("app");
    }

    public static IDbManager getMysqlConnection() {
        SimpleConfigurator.addConfigurator(new DbConfig(SqliteUtil.getProperty("jdbc_url") + "", SqliteUtil.getProperty("jdbc_username"), SqliteUtil.getProperty("jdbc_password"), "mysql"));
        ConfigCenter.INSTANCE.setSqlout(new SimpleSqlout());
        return new SingleManager("mysql");
    }

    public static IDbManager getOracleConnection() {
        //&useOldAliasMetadataBehavior=true&UseOldSyntax=true
        SimpleConfigurator.addConfigurator(new DbConfig(DbConfig.DRIVER_ORACLE, SqliteUtil.getProperty("jdbc_url"), SqliteUtil.getProperty("jdbc_username"), SqliteUtil.getProperty("jdbc_password"), "oracle"));
        ConfigCenter.INSTANCE.setSqlout(new SimpleSqlout());
        return new SingleManager("oracle");
    }

    public static IDbManager getSqliteConnection() {
//        SimpleConfigurator.addConfigurator(new DbConfig("org.sqlite.JDBC", "jdbc:sqlite:data.db", "", "", "sqlite"));
        return SqliteUtil.getManager();
    }

    public static IDbManager getCurrentTemplateSource() {
        int loadSource = SqliteUtil.getLongProperty("loadSource").intValue();
        switch (loadSource) {
            case LOAD_SOURCE_SQLITE:
                return getSqliteConnection();
            case LOAD_SOURCE_DB:
                String template_jdbc_url = SqliteUtil.getProperty("template_jdbc_url");
                String template_jdbc_username = SqliteUtil.getProperty("template_jdbc_username");
                String template_jdbc_password = SqliteUtil.getProperty("template_jdbc_password");
                if (ObjectHelper.isAllNotEmpty(template_jdbc_url, template_jdbc_username, template_jdbc_password)) {
                    SimpleConfigurator.addConfigurator(new DbConfig(template_jdbc_url, template_jdbc_username, template_jdbc_password, "loadSource"));
                    return new SingleManager("loadSource");
                } else {
                    JOptionPane.showMessageDialog(null, "请先配置模板数据源");
                    return null;
                }
        }
        return null;
    }
}
