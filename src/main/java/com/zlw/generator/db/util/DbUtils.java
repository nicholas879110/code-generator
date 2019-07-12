package com.zlw.generator.db.util;

import com.zlw.generator.db.ModuleBean;
import com.zlw.generator.db.crud.DataBaseManager;
import com.zlw.generator.db.crud.DatabaseAccess;
import com.zlw.generator.db.domain.ColumnInfo;
import com.zlw.generator.db.domain.DicBean;
import com.zlw.generator.db.domain.EnumBean;
import com.zlw.generator.db.domain.TableInfo;
import com.zlw.generator.db.transaction.IDbManager;
import com.zlw.generator.util.ObjectHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class DbUtils {
    protected static Logger log = LoggerFactory.getLogger(DbUtils.class);

    static {
        if (!SqliteUtil.tableExist("TableInfo")) {
            SqliteUtil.execute("create table TableInfo (tablename text,entyname text,tablecomment text)");
        }
        if (!SqliteUtil.tableExist("ColumnInfo")) {
            SqliteUtil.execute("create table ColumnInfo (tablename text,columnname text,fieldname text ,columncomment text)");
        }
        if (!SqliteUtil.tableExist("DicBean")) {
            SqliteUtil.execute("create table DicBean (columnname text,english text,times int)");
        }
        if (!SqliteUtil.tableExist("ModuleBean")) {
            SqliteUtil.execute("create table ModuleBean (serial text primary key  ,modulename text,fileext text,templatefile text,isedit boolean)");
        }
        if (!SqliteUtil.tableExist("template")) {
            SqliteUtil.execute("create table template (id INTEGER PRIMARY KEY AUTOINCREMENT,absolutePath text,'text' text,fileName text,isGloable int)");
        }
    }

    public static TableInfo getInfoByName(String name, IDbManager manager) throws SQLException {
        TableInfo info = new TableInfo();
        DataBaseManager hyberbin = new DataBaseManager(info, manager, false);
        hyberbin.addParmeter(name);
        hyberbin.showOne("select * from TableInfo where tableName=?");
        return info;
    }

    public static void saveOrUpdateTableInfo(TableInfo tableInfo, IDbManager manager) throws SQLException {
        TableInfo info = new TableInfo();
        DataBaseManager hyberbin = new DataBaseManager(info, manager, false);
        hyberbin.addParmeter(tableInfo.getTablename());
        hyberbin.showOne("select * from TableInfo where tableName=?");
        if (info.getTablename() == null) {
            new DataBaseManager(tableInfo, manager).insert("");
        } else {
            new DataBaseManager(tableInfo, manager).updateByKey("tablename");
        }

    }

    public static ColumnInfo getColumnInfo(String tname, String cname, IDbManager manager) throws SQLException {
        ColumnInfo info = new ColumnInfo();
        DataBaseManager hyberbin = new DataBaseManager(info, manager, false);
        hyberbin.addParmeter(tname).addParmeter(cname);
        hyberbin.showOne("select * from ColumnInfo where tableName=? and columnname=?");
        return info;

    }

    public static ModuleBean getModuleInfo(String serial, IDbManager manager) throws SQLException {
        ModuleBean info = new ModuleBean();
        DataBaseManager hyberbin = new DataBaseManager(info, manager, false);
        hyberbin.addParmeter(serial);
        hyberbin.showOne("select * from ModuleBean where serial=?");
        return info;
    }

    public static List<ModuleBean> getModuleInfo(IDbManager manager) throws SQLException {
        ModuleBean moduleBean = new ModuleBean();
        DataBaseManager hyberbin = new DataBaseManager(moduleBean, manager, false);
        List<ModuleBean> list = hyberbin.showList("select * from ModuleBean ");
        return list;
    }

    public static void saveOrUpdateColumnInfo(ColumnInfo Info, IDbManager manager) throws SQLException {
        ColumnInfo columnInfo = getColumnInfo(Info.getTablename(), Info.getColumnname(), manager);
        DataBaseManager hyberbin = new DataBaseManager(Info, manager, false);
        if (columnInfo.getColumnname() == null) {
            hyberbin.insert("");
        } else {
            DatabaseAccess access = new DatabaseAccess(manager);
            access.setParmeter(Info.getFieldname()).setParmeter(Info.getColumncomment()).setParmeter(Info.getTablename()).setParmeter(Info.getColumnname());
            access.update("update ColumnInfo set fieldname=? , columncomment=? where tablename=? and columnname=?");
        }

    }

    public static EnumBean getEnumInfo(String tname, String dm, IDbManager manager) throws SQLException {
        EnumBean info = new EnumBean();
        DataBaseManager hyberbin = new DataBaseManager(info, manager, false);
        hyberbin.addParmeter(tname).addParmeter(dm);
        hyberbin.showOne("select * from EnumBean where tableName=? and dm=?");
        return info;

    }

    public static void saveOrUpdateEnumInfo(EnumBean Info, IDbManager manager) throws SQLException {
        EnumBean bean = getEnumInfo(Info.getTablename(), Info.getDm(), manager);
        DataBaseManager hyberbin = new DataBaseManager(Info, manager, false);
        if (bean.getDm() == null) {
            hyberbin.insert("");
        } else {
            DatabaseAccess access = new DatabaseAccess(manager);
            access.setParmeter(Info.getEnglish()).setParmeter(Info.getPingying()).setParmeter(Info.getTablename()).setParmeter(Info.getDm());
            access.update("update EnumBean set English=?, Pingying=? where tablename=? and dm=?");
        }

    }

    public static DicBean getDic(DicBean bean, IDbManager manager) throws SQLException {
        DataBaseManager hyberbin = new DataBaseManager(bean, manager, false);
        StringBuilder sql = new StringBuilder("select columnname,english,times from dicbean where columnname=? ");
        hyberbin.addParmeter(bean.getColumnname());
        if (!ObjectHelper.isNullOrEmptyString(bean.getEnglish())) {
            sql.append(" and english=? ");
            hyberbin.addParmeter(bean.getEnglish());
        }
        sql.append(" order by times desc limit 1");
        List<DicBean> showList = hyberbin.showList(sql.toString());
        if (ObjectHelper.isEmpty(showList)) {
            return null;
        }
        return showList.get(0);

    }

    public static void addDicBean(DicBean bean, IDbManager manager) throws SQLException {
        if (getDic(bean, manager) == null) {
            DataBaseManager hyberbin = new DataBaseManager(bean, manager, false);
            hyberbin.insert("");
        } else {
            DatabaseAccess access = new DatabaseAccess(manager);
            access.setParmeter(bean.getColumnname()).setParmeter(bean.getEnglish());
            access.update("update dicbean set times=times+1 where columnname=? and english=?");
        }

    }

    public static void clearTableEnglish(IDbManager manager, String... table) throws SQLException {
        DatabaseAccess access = new DatabaseAccess(manager);
        StringBuilder sql = new StringBuilder("update TableInfo set entyname ='',tablecomment='' where tableName in('null'");
        for (String tableName : table) {
            access.setParmeter(tableName);
            sql.append(",?");
        }
        sql.append(")");
        access.update(sql.toString());

    }

    public static void clearColumnEnglish(String table, IDbManager manager) throws SQLException {
        DatabaseAccess access = new DatabaseAccess(manager);
        String sql = "update ColumnInfo set fieldname ='',columncomment='' where tableName=?";
        access.setParmeter(table);
        access.update(sql);

    }

    public static void clearEnumEnglish(String table, IDbManager manager) throws SQLException {
        DatabaseAccess access = new DatabaseAccess(manager);
        String sql = "update EnumBean set English ='' where tableName=?";
        access.setParmeter(table);
        access.update(sql);

    }

    public static void saveOrUpdateModuleInfo(ModuleBean Info, IDbManager manager) throws SQLException {
        ModuleBean moduleBeanDB = getModuleInfo(Info.getSerial(), manager);
        DataBaseManager hyberbin = new DataBaseManager(Info, manager, false);
        if (moduleBeanDB.getSerial() == null) {
            hyberbin.insert("");
        } else {
            DatabaseAccess access = new DatabaseAccess(manager);
            access.setParmeter(Info.getModuleName()).setParmeter(Info.getFileExt()).setParmeter(Info.getTemplateFile()).setParmeter(Info.getIsEdit()).setParmeter(Info.getSerial());
            access.update("update ModuleBean set modulename=? ,fileext=?,templatefile=?,isedit=? where serial=? ");
        }
    }

    public static void deleteModuleInfo(ModuleBean info, IDbManager manager) throws SQLException {
        ModuleBean moduleBeanDB = getModuleInfo(info.getModuleName(), manager);
        if (StringUtils.isNotBlank(moduleBeanDB.getModuleName())) {
            DatabaseAccess access = new DatabaseAccess(manager);
            access.setParmeter(info.getModuleName());
            access.delete("delete from  ModuleBean  where modulename=? ");
        }
    }

//    public static Template getTemplateByPathAndName(String path,String name, IDbManager manager) throws SQLException {
//        Template template = new Template();
//        Hyberbin<Template> hyberbin = new Hyberbin<Template>(template, manager);
//        hyberbin.addParmeter(path,name);
//        return hyberbin.showOne("select * from template where absolutePath=? and fileName=? ");
//
//    }
//
//    public static void saveTemplate(Template template, IDbManager manager) throws SQLException {
//        Hyberbin<Template> hyberbin = new Hyberbin<Template>(template, manager);
//        hyberbin.insert("id");
//    }
//
//    public static void updateTemplate(Template template, IDbManager manager) throws SQLException {
//        Hyberbin<Template> hyberbin = new Hyberbin<Template>(template, manager);
//        hyberbin.updateByKey("id");
//    }
//
//    public static void saveOrUpdateTemplate(Template template, IDbManager manager) throws SQLException {
//        Template templateByPath = getTemplateByPathAndName(template.getAbsolutePath(),template.getFileName(), manager);
//        if (ObjectHelper.isNullOrEmptyString(templateByPath.getId())) {
//            saveTemplate(template, manager);
//        } else {
//            template.setId(templateByPath.getId());
//            updateTemplate(template, manager);
//        }
//    }
//
//    public static void deleteTemplate(String path,String fileName, IDbManager manager) throws SQLException {
//        Template template = new Template();
//        Hyberbin<Template> hyberbin = new Hyberbin<Template>(template, manager);
//        hyberbin.addParmeter(path,fileName);
//        hyberbin.delete(" where absolutePath=? and fileName=? ");
//    }
}
