package com.zlw.generator.plugin;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SettingHelper {

    private static final Logger logger = LoggerFactory.getLogger(SettingHelper.class);

    private static final String DEFAULT_SETTING_PATH = System.getProperty("user.home") + File.separator + ".codegenerator";
    private static final String DEFAULT_SETTING_FILE = DEFAULT_SETTING_PATH + File.separator + "codegenerator.properties";

    private static Properties settingProp = null;

    static {
        settingProp = new Properties();
        try {
            init();
            settingProp.load(new FileInputStream(DEFAULT_SETTING_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init() throws IOException {
        File settingDir = new File(DEFAULT_SETTING_PATH);
        if (!settingDir.exists()) {
            settingDir.mkdirs();
        }
        File settingFile = new File(settingDir.getAbsolutePath() + File.separator + "codegenerator.properties");
        if (!settingFile.exists()) {
            settingFile.createNewFile();
        }
    }

    public static boolean checkDataDir() {
        if (settingProp != null) {
            String dataDir = settingProp.getProperty("data.dir");
            String templateDir = settingProp.getProperty("template.dir");
            //        String logDir = settingProp.getProperty("log.dir");
            if (StringUtils.isNotBlank(dataDir) && StringUtils.isNotBlank(templateDir)) {
                File file = new File(dataDir);
                if (file.exists() && file.isDirectory()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void settting(String basePath) {

        //设置数据目录
        File dataDir = new File(basePath + File.separator + "data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File templateDir = new File(basePath + File.separator + "template");
        if (!templateDir.exists()) {
            templateDir.mkdirs();
        }

        settingProp.put("data.dir", dataDir.getAbsolutePath());
        settingProp.put("template.dir", templateDir.getAbsolutePath());

        String sqlliteUrl = dataDir.getAbsolutePath() + File.separator + "data.db";
        settingProp.put("sqlliteUrl.url", sqlliteUrl);

        try {
            settingProp.store(new FileOutputStream(DEFAULT_SETTING_FILE), "setting");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyResource(String basePath) throws IOException {
        File templateDir = new File(basePath + File.separator + "template");
        String path1 = SettingHelper.class.getClassLoader().getResource("").getPath();
        System.out.println(path1);

        String path = SettingHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String path2 = SettingHelper.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        logger.info("path:{}", path);
//        File file = new File(path);
        path = URLDecoder.decode(path, "UTF-8");
        JarFile localJarFile = new JarFile(new File(path));
        Enumeration<JarEntry> entries = localJarFile.entries();
        logger.info("copy resource start:{}", path);
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String innerPath = jarEntry.getName();
            logger.info("innerPath:{}", innerPath);
            if (StringUtils.startsWith(innerPath, "template")&&!StringUtils.startsWith(innerPath, "templates")&&!StringUtils.equals(innerPath,"template/")&&!StringUtils.equals(innerPath,"template\\")) {
                InputStream inputStream = SettingHelper.class.getClassLoader().getResourceAsStream(innerPath);

                String nameX = StringUtils.substringAfterLast(innerPath, "/");
                logger.info("name:{}", nameX);
//                nameX = StringUtils.substringAfterLast(nameX, "\\");
//                logger.info("name:{}", nameX);
                IOUtils.copy(inputStream, new FileOutputStream(new File(templateDir + File.separator + nameX)));
            }

        }
        logger.info("copy resource end");
    }

    public static void clear() {

    }

    public static String getValue(String key) {
        return settingProp.getProperty(key);
    }

    public static void main(String[] args) throws IOException {
//        String path = SettingHelper.class.getClassLoader().getResource("template").getFile();
//        System.out.println(path);

//        String path =SettingHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String path="/C:/Users/King L Zhang/AppData/Local/Temp/e4j7CE6.tmp_dir1559787922/code-generator-1.0.0-SNAPSHOT.jar";
//        System.out.println(path);
//        JarFile localJarFile = new JarFile(new File(path));
//
//        Enumeration<JarEntry> entries = localJarFile.entries();
//        logger.info("copy resource start:{}", path);
//        while (entries.hasMoreElements()) {
//            JarEntry jarEntry = entries.nextElement();
//            String innerPath = jarEntry.getName();
//            logger.info("innerPath:{}",innerPath);
//            if(StringUtils.isNotBlank(innerPath)){
//                InputStream inputStream = SettingHelper.class.getClassLoader().getResourceAsStream(innerPath);
////                String name=StringUtils.substringAfterLast(innerPath,"/");
////                IOUtils.copy(inputStream, new FileOutputStream(new File(templateDir + File.separator +name )));
//            }
//
//        }

        String path = "C:/Users/King%20L%20Zhang/AppData/Local/Temp/e4j495D.tmp_dir1559788630/";
        System.out.println(path);
        System.out.println(URLDecoder.decode(path, "UTF-8"));


        String nameX = StringUtils.substringAfterLast("template/addJspTemplate.ftl", "/");
        logger.info("name:{}", nameX);
        nameX = StringUtils.substringAfterLast(nameX, "\\");
        logger.info("name:{}", nameX);
    }
}
