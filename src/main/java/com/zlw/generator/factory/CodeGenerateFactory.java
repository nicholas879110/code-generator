package com.zlw.generator.factory;

import com.zlw.generator.db.Constants;
import com.zlw.generator.db.ModuleBean;
import com.zlw.generator.db.domain.TableBean;
import com.zlw.generator.db.util.DbUtils;
import com.zlw.generator.db.util.SqliteUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CodeGenerateFactory {

    private static final Logger logger = LoggerFactory.getLogger(CodeGenerateFactory.class);

    public static void codeGenerate(TableBean createBean) {

        String tableName = createBean.getTableName();

        String packageName = SqliteUtil.getProperty(Constants.PACKAGE_NAME);
        String className = StringUtils.isEmpty(createBean.getEntityName()) ? createBean.getTablesNameToClassName(tableName) : createBean.getEntityName();
        String lowerName = className.substring(0, 1).toLowerCase() + className.substring(1, className.length());

        String projectPath = SqliteUtil.getProperty(Constants.PROJECT_PATH) + "\\";
        java.util.List<ModuleBean> modules = new ArrayList<>();
        modules.addAll(Constants.DEFAULT_MODULES);
        try {
            List<ModuleBean> list = DbUtils.getModuleInfo(Constants.getSqliteConnection());
            modules.addAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        VelocityContext context = new VelocityContext();
        context.put("className", className);
        context.put("lowerName", lowerName);
        context.put("tableName", tableName);
//        context.put("entityPackage", entityPackage);
//        context.put("moduleName", moduleName);
        context.put("descn", createBean.getTableComment());
        context.put("pk", createBean.getPrimaryKey());
        context.put("date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        context.put("author", SqliteUtil.getProperty("author"));
        try {
            context.put("feilds", createBean.getBeanFeilds());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Map sqlMap = createBean.getAutoCreateSql();
            context.put("columnDatas", createBean.getColumnList());
            context.put("SQL", sqlMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (ModuleBean module : modules) {
            Boolean isGenerate = SqliteUtil.getBoolProperty(Constants.MODULE_IS_GENERATE + module.getModuleName());
            if (isGenerate != null && isGenerate) {
                String path = projectPath + SqliteUtil.getProperty(Constants.MODULE_PREFIX + module.getModuleName());
                //默认工程为maven工程("增加源码路径src/main/java，单元测试路径src/main/test")
                if (module.getFileExt().trim().equalsIgnoreCase(".java")) {
                    if (module.getModuleName().trim().equalsIgnoreCase("test")) {
                        path += "\\src\\main\\test\\";
                    } else {
                        path += "\\src\\main\\java\\";
                    }
                }
                //路径增加包名路径
                String[] packages = packageName.split("\\.");
                for (String p : packages) {
                    path += p + "\\";
                }
                //路径增加module
                String ms[] = module.getModuleName().split("\\.");
                String name = "";
                String templateName = "";

                String entityPackage = packageName + "." + module.getModuleName();
                context.put("entityPackage", entityPackage);
                if (module.getModuleName().equalsIgnoreCase("entity")) {
                    name = className;
                    templateName = module.getModuleName().substring(0, 1).toUpperCase() + module.getModuleName().substring(1);
                    path += module.getModuleName() + "\\";
                } else {
                    name += className;
                    for (String m : ms) {
                        name += m.substring(0, 1).toUpperCase() + m.substring(1);
                        templateName += m.substring(0, 1).toUpperCase() + m.substring(1);
                        path += m + "\\";
                    }
                }
                name += module.getFileExt();

//                String templatePath = "";
//                if (Constants.DEFAULT_MODULES.contains(module)) {
//                    templatePath += SettingHelper.getValue("template.dir") + File.separator + ;
//                } else {
//                    templatePath += SettingHelper.getValue("template.dir") + File.separator + module.getTemplateFile();
//                }
                CommonPageParser.WriterPage(context, module.getTemplateFile(), path, name);
            }

        }


//        String webPath = SqliteUtil.getProperty("basePath") + "\\view\\" + moduleName + "\\" + lowerName + "\\";

        String modelPath = "page\\" + "" + className + "Page.java";
        String beanPath = "entity\\" + "" + className + ".java";
        String mapperPath = "dao\\" + "" + className + "Dao.java";
        String servicePath = "service\\" + "" + className + "Service.java";
        String serviceImplPath = "service\\impl\\" + "" + className + "ServiceImpl.java";
        String controllerPath = "controller\\" + "" + className + "Controller.java";
        String sqlMapperPath = "mapper\\" + "" + className + "Mapper.xml";

        String facadePath = "facade\\" + "" + className + "Facade.java";
        String facadeImplPath = "facade\\impl\\" + "" + className + "FacadeImpl.java";

        String openApi = "openApi\\" + "" + className + "OpenApi.java";

//        String jspPath = webPath + "\\";
//        String jsPath = webPath + "\\";
//
//        String listJsp = "list.jsp";
//        String addJsp = "add.jsp";
//        String editJsp = "edit.jsp";
//        String detailJsp = "detail.jsp";
//
//        String listJs = "list.js";
//        String addJs = "add.js";
//        String editJs = "edit.js";
//        String detailJs = "detail.js";


//        CommonPageParser.WriterPage(context, "EntityTemplate.ftl", pckPath, beanPath);
        //     CommonPageParser.WriterPage(context, "PageTemplate.ftl", pckPath, modelPath);
//        CommonPageParser.WriterPage(context, "DaoTemplate.ftl", pckPath, mapperPath);
//        CommonPageParser.WriterPage(context, "ServiceTemplate.ftl", pckPath, servicePath);
//        CommonPageParser.WriterPage(context, "ServiceTemplateImpl.ftl", pckPath, serviceImplPath);
//        CommonPageParser.WriterPage(context, "MapperTemplate.xml", pckPath, sqlMapperPath);
//        CommonPageParser.WriterPage(context, "FacadeTemplate.ftl", pckPath, facadePath);
//        CommonPageParser.WriterPage(context, "FacadeTemplateImpl.ftl", pckPath, facadeImplPath);
//        CommonPageParser.WriterPage(context, "ControllerTemplate.ftl", pckPath, controllerPath);
//        CommonPageParser.WriterPage(context, "OpenApiTemplate.ftl", pckPath, openApi);
        //jsp
//        CommonPageParser.WriterPage(context, "listJspTemplate.ftl", jspPath, listJsp);
//        CommonPageParser.WriterPage(context, "addJspTemplate.ftl", jspPath, addJsp);
//        CommonPageParser.WriterPage(context, "editJspTemplate.ftl", jspPath, editJsp);
//        CommonPageParser.WriterPage(context, "detailJspTemplate.ftl", jspPath, detailJsp);
//        //js
//        CommonPageParser.WriterPage(context, "listJsTemplate.ftl", jsPath, listJs);
//        CommonPageParser.WriterPage(context, "addJsTemplate.ftl", jsPath, addJs);
//        CommonPageParser.WriterPage(context, "editJsTemplate.ftl", jsPath, editJs);
//        CommonPageParser.WriterPage(context, "detailJsTemplate.ftl", jsPath, detailJs);
    }

    public static String getProjectPath() {
        String path = System.getProperty("user.dir").replace("\\", "/") + "/";
        return path;
    }

    public static void main(String[] args) {
        String s = "com.zlw.mine.tax.adminstrator";
        String sss[] = s.split("\\.");
        System.out.println(sss.length);
    }

}
