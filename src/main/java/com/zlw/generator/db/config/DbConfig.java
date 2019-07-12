package com.zlw.generator.db.config;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class DbConfig {

    public static final String DEFAULT_CONFIG_NAME = "default";
    public static final String DRIVER_SQLITE = "org.sqlite.JDBC";
    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
    public static final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String URL_MYSQL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true&useOldAliasMetadataBehavior=true&UseOldSyntax=true";
    public static final String URL_ORACLE = "jdbc:oracle:thin:@localhost:1521:ORCL";
    public static final String URL_SQLITE = "jdbc:sqlite:data.db";
    public static final String URL_SQLSERVER = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName";
    public static final String USER = "root";
    public static final String PASS = "root";
    private String driver;
    private String url;
    private String username;
    private String password;
    private final String configName;

    public DbConfig(String driver, String url, String username, String password, String configName) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.configName = configName;
    }

    public DbConfig(String url, String username, String password, String configName) {
        this(getDriverFromUrl(url), url, username, password, configName);
    }

    public DbConfig(String url, String configName) {
        this(getDriverFromUrl(url), url, "root", "root", configName);
    }

    public DbConfig(String configName) {
        this("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true&useOldAliasMetadataBehavior=true&UseOldSyntax=true", "root", "root", configName);
    }

    public DbConfig() {
        this("default");
    }

    public String getDriver() {
        return this.driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfigName() {
        return this.configName;
    }

    public static String getMysqlUrl(String ip, String dbname, int port) {
        //&generateSimpleParameterMetadata=true&useOldAliasMetadataBehavior=true&UseOldSyntax=true
        return "jdbc:mysql://" + ip + ":" + port + "/" + dbname + "?useUnicode=true&characterEncoding=UTF-8";
    }

    public static String getMysqlUrl(String ip, String dbname) {
        return getMysqlUrl(ip, dbname, 3306);
    }

    public static String getDriverFromUrl(String url) {
        return url.contains("oracle")?"oracle.jdbc.driver.OracleDriver":(url.contains("mysql")?"com.mysql.jdbc.Driver":(url.contains("sqlite")?"org.sqlite.JDBC":(url.contains("sqlserver")?"com.microsoft.sqlserver.jdbc.SQLServerDriver":"com.mysql.jdbc.Driver")));
    }
}
