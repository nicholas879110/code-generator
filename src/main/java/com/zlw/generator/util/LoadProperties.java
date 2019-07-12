package com.zlw.generator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class LoadProperties {
    private static final Logger log = LoggerFactory.getLogger(LoadProperties.class);
    private final java.util.Properties props = new SafeProperties();
    private String filePath;

    public LoadProperties(String filePath) {
        Object in = null;

        try {
            if(!filePath.replace("\\", "/").contains("/")) {
                filePath = filePath.replace(".", "/");
                String ex = filePath.substring(filePath.lastIndexOf("/")).replace("/", ".");
                filePath = filePath.substring(0, filePath.lastIndexOf("/")) + ex;
                this.filePath = filePath;
                log.debug(filePath);
                in = this.getClass().getClassLoader().getResource(filePath).openStream();
            } else {
                this.filePath = URLDecoder.decode(filePath, "utf-8");
                log.debug(this.filePath);
                File ex1 = new File(filePath);
                this.filePath = ex1.getAbsolutePath();
                in = new FileInputStream(this.filePath);
            }

            this.props.load((InputStream)in);
        } catch (IOException var12) {
            log.error("LoadProperties找不到数据库配置文件，请检查缺省包下面的{}文件!", filePath, var12);
        } finally {
            if(in != null) {
                try {
                    ((InputStream)in).close();
                } catch (IOException var11) {
                    log.error("LoadProperties找不到数据库配置文件，请检查缺省包下面的{}文件!", filePath, var11);
                }
            }

        }

    }

    public LoadProperties(InputStream in) {
        try {
            this.props.load(in);
        } catch (IOException var11) {
            log.error("LoadProperties找不到配置文件", var11);
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException var10) {
                    log.error("LoadProperties找不到数据库配置文件", var10);
                }
            }

        }

    }

    public Object loadProperties(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();

        try {
            Field[] ex = fields;
            int var4 = fields.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = ex[var5];
                String name = field.getName();
                String pValue = this.props.getProperty(field.getName());
                if(pValue != null) {
                    Object value = ConverString.asType(field.getType(), pValue);
                    FieldUtil.setFieldValue(object, name, value);
                }
            }
        } catch (Exception var10) {
            log.error("LoadProperties读取配置文件错误\t", var10);
        }

        return object;
    }

    public java.util.Properties getProps() {
        return this.props;
    }

    public void store() {
        try {
            FileOutputStream ex = new FileOutputStream(this.filePath);
            this.props.store(ex, "");
            ex.flush();
            ex.close();
        } catch (IOException var2) {
            log.error("LoadProperties写入{}文件失败\t", this.filePath, var2);
        }

    }

    public String getProperty(String name) {
        Object get = this.props.get(name);
        return get == null?null:get.toString();
    }

    public int getIntProperties(String name) {
        return Integer.parseInt(this.getProperty(name));
    }

    public boolean getBoolProperties(String name) {
        return Boolean.TRUE.equals(this.props.get(name)) || "true".equals(this.getProperty(name));
    }
}
