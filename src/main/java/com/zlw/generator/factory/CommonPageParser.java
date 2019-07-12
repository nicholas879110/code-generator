package com.zlw.generator.factory;

import com.zlw.generator.plugin.SettingHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class CommonPageParser {
	
//    private static VelocityEngine ve;

    private static final String CONTENT_ENCODING = "UTF-8";


    private static boolean isReplace = true;

    static {
        try {
//            ClassPathResource templateResource = new ClassPathResource("template");
//            String templateBasePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//            System.out.println(templateBasePath);
//            Properties properties = new Properties();
//            properties.setProperty("resource.loader", "file");
//            properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
//            properties.setProperty("file.resource.loader.path", templateBasePath);
//            properties.setProperty("file.resource.loader.cache", "true");
//            properties.setProperty("file.resource.loader.modificationCheckInterval", "30");
//            properties.setProperty("directive.set.null.allowed", "true");
//            properties.setProperty("runtime.log", "logs/velocity.log");
//            properties.setProperty("input.encoding", CONTENT_ENCODING);
//            properties.setProperty("output.encoding ", CONTENT_ENCODING);
//            VelocityEngine velocityEngine = new VelocityEngine();
//            velocityEngine.init(properties);
//            ve = velocityEngine;
        } catch (Exception e) {
            e.printStackTrace();
        	System.out.println(e.getMessage());
        }
    }

    public static void WriterPage(VelocityContext context, String templatePath, String fileDirPath, String targetFile) {
        try {
//            ClassPathResource templateResource = new ClassPathResource("template");
//            String templateBasePath = CommonPageParser.class.getClass().getClassLoader().getResource("template").getPath();
//            System.out.println("tmplate:"+templateBasePath);
            Properties properties = new Properties();
//            properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//            properties.setProperty("resource.loader", "file");
//            properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
//            properties.setProperty("file.resource.loader.path", "template");
//            properties.setProperty("file.resource.loader.cache", "true");
//            properties.setProperty("file.resource.loader.modificationCheckInterval", "30");
//            properties.setProperty("directive.set.null.allowed", "true");
            properties.setProperty("runtime.log", "logs/velocity.log");
            properties.setProperty("input.encoding", CONTENT_ENCODING);
            properties.setProperty("output.encoding ", CONTENT_ENCODING);
            properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, SettingHelper.getValue("template.dir"));
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init(properties);
//            ve = velocityEngine;
            File file = new File(fileDirPath + targetFile);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
            } else if (isReplace) {
               System.out.println("替换文件:" + file.getAbsolutePath());
            }


            Template template = velocityEngine.getTemplate(templatePath, "UTF-8");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            template.merge(context, writer);
            writer.flush();
            writer.close();
            fos.close();
            System.out.println("生成文件：" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        	System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println( CommonPageParser.class.getClass().getResource("/").getPath());;
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        System.out.println(CommonPageParser.class.getClass().getResource("template").getPath());
    }
}