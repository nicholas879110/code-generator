package com.zlw.generator.db.crud;

import com.zlw.generator.db.bean.FieldColumn;
import com.zlw.generator.db.bean.TableBean;
import com.zlw.generator.db.config.ConfigCenter;
import com.zlw.generator.db.page.Pager;
import com.zlw.generator.db.transaction.IDbManager;
import com.zlw.generator.db.util.CacheFactory;
import com.zlw.generator.util.*;
import org.jsoup.select.Evaluator;

import com.zlw.generator.persistence.FetchType;
import com.zlw.generator.persistence.JoinColumn;
import com.zlw.generator.persistence.ManyToOne;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class DataBaseManager<T> extends BaseDbTool {

    private String tableName;
    private List<FieldColumn> fields = null;
    private boolean firstAddField = true;
    private T po;
    private boolean updateNull = false;
    private List<FieldColumn> nuList;
    private String primaryKey = "id";
    private boolean superField = true;

    private void ini(T po) {
        if(po != null) {
            try {
                TableBean ex = CacheFactory.MINSTANCE.getHyberbin(po.getClass(), this.superField);
                this.po = po;
                this.tableName = ex.getTableName();
                this.primaryKey = ex.getPrimaryKey();
                this.fields = new ArrayList(ex.getColumns());
            } catch (SecurityException var3) {
                this.log.error("初始化错误", var3);
            }
        }

    }

    private T getPo() {
        if(this.po == null) {
            this.log.error("po值为空，注意：空构造方法不能执行此操作");
        }

        return this.po;
    }

    public DataBaseManager(T tablebean) {
        super(tablebean instanceof IDbManager?(IDbManager)tablebean:ConfigCenter.INSTANCE.getManager());
        if(!(tablebean instanceof IDbManager)) {
            this.ini(tablebean);
        }

    }

    public DataBaseManager() {
        super(ConfigCenter.INSTANCE.getManager());
    }

    public DataBaseManager(T tablebean, IDbManager tx) {
        super(tx);
        this.ini(tablebean);
    }

    public DataBaseManager(T tablebean, boolean superField) {
        super(tablebean instanceof IDbManager?(IDbManager)tablebean:ConfigCenter.INSTANCE.getManager());
        if(!(tablebean instanceof IDbManager)) {
            this.superField = superField;
            this.ini(tablebean);
        }

    }

    public DataBaseManager(T tablebean, IDbManager tx, boolean superField) {
        super(tx);
        this.superField = superField;
        this.ini(tablebean);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public DataBaseManager removeField(String fieldName) {
        this.log.trace("in removeField,fieldName{}", fieldName);
        if(fieldName != null && !"".equals(fieldName.trim())) {
            FieldColumn fieldColumn = FieldUtil.getFieldColumnByCache(FieldUtil.getField(this.getPo().getClass(), fieldName));
            this.fields.remove(fieldColumn);
            this.log.debug("不查fieldName：{},column:{}", fieldName, fieldColumn.getColumn());
            return this;
        } else {
            return this;
        }
    }

    public DataBaseManager setField(String fieldName) {
        this.log.trace("in setField,fieldName:{}", fieldName);
        if(this.fields == null || this.firstAddField) {
            this.fields = new ArrayList(0);
            this.firstAddField = false;
        }

        FieldColumn fieldColumn = FieldUtil.getFieldColumnByCache(FieldUtil.getField(this.getPo().getClass(), fieldName));
        this.fields.add(fieldColumn);
        this.log.debug("加入fieldName:{},column:{}", fieldName, fieldColumn.getColumn());
        return this;
    }

    private String getFieldList() {
        char[] quote = this.adapter.getQuote();
        if(this.fields != null) {
            StringBuilder fieldlist = new StringBuilder();
            Iterator var3 = this.fields.iterator();

            while(var3.hasNext()) {
                FieldColumn field = (FieldColumn)var3.next();
                if(!field.isIgnore()) {
                    fieldlist.append(",").append(quote[0]).append(field.getColumn()).append(quote[1]);
                }
            }

            return fieldlist.substring(1);
        } else {
            return "";
        }
    }

    public List<FieldColumn> getFieldColumns() {
        return this.fields;
    }

    private Object loadData(Object table, ResultSet rs) throws Exception {
        this.log.trace("in loadData");
        Iterator var3 = this.fields.iterator();

        while(var3.hasNext()) {
            FieldColumn fieldColumn = (FieldColumn)var3.next();
            if(!fieldColumn.isIgnore()) {
                Field field = fieldColumn.getField();
                if(field.isAnnotationPresent(JoinColumn.class)) {
                    this.log.trace("has joinColumn field:{}", field.getName());
                    Object newInstance = field.getType().newInstance();
                    FieldColumn joinColumn = fieldColumn.cloneMe(FieldUtil.getField(field.getType(), Evaluator.Id.class));
                    this.loadDataToPojo(newInstance, joinColumn, rs);
                    FieldUtil.setFieldValue(table, field.getName(), newInstance);
                    if(field.isAnnotationPresent(ManyToOne.class)) {
                        this.log.trace("manyToOne field:{}", field.getName());
                        ManyToOne manyToOne = (ManyToOne)field.getAnnotation(ManyToOne.class);
                        if(manyToOne.fetch() == FetchType.EAGER) {
                            this.log.trace("frtch is FetchType.EAGER");
                            (new DataBaseManager(newInstance)).showOnebyKey(joinColumn.getField().getName());
                        }
                    }
                } else {
                    this.loadDataToPojo(table, fieldColumn, rs);
                }
            }
        }

        return table;
    }

    private List loadListData(Object table, ResultSet rs) {
        this.log.trace("in loadListData");
        ArrayList list = new ArrayList(0);

        try {
            while(rs != null && rs.next()) {
                list.add(this.loadData(table.getClass().newInstance(), rs));
            }

            return list;
        } catch (Exception var5) {
            throw new IllegalArgumentException("loadData error!", var5);
        }
    }

    private void loadDataToPojo(Object table, FieldColumn fieldColumn, ResultSet rs) throws SQLException {
        this.log.trace("in loadDataToPojo ");
        Class type = fieldColumn.getField().getType();
        Object getResultSet = rs.getObject(fieldColumn.getColumn(), type);
        if(getResultSet == null) {
            getResultSet = rs.getObject(fieldColumn.getColumn());
        }

        if(getResultSet != null && !type.isAssignableFrom(getResultSet.getClass())) {
            getResultSet = ConverString.asType(type, getResultSet);
        }

        this.log.trace("loaded Data object:{},field:{},column:{},value:{}", new Object[]{table.getClass().getSimpleName(), fieldColumn.getField().getName(), fieldColumn.getColumn(), getResultSet});
        if(fieldColumn.isHasGetterAndSetter()) {
            this.log.trace("Class {} has getter and setter,use local getter and setter", table.getClass());
            ReflectionUtils.invokeSetter(table, fieldColumn.getField().getName(), getResultSet, type);
        } else {
            this.log.trace("Class {} do not has getter and setter,use local getter and setter", table.getClass());

            try {
                fieldColumn.getField().set(table, getResultSet);
            } catch (IllegalArgumentException var7) {
                Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, (String)null, var7);
            } catch (IllegalAccessException var8) {
                this.log.error("Class {} do not have setter and getter and set value error!", table.getClass(), var8);
            }
        }

    }

    private GetSql getSql() {
        GetSql gs = new GetSql();
        Iterator var2 = this.fields.iterator();

        FieldColumn nullF;
        while(var2.hasNext()) {
            nullF = (FieldColumn)var2.next();
            if(!nullF.isIgnore()) {
                Object value = nullF.getField().isAnnotationPresent(JoinColumn.class)?FieldUtil.getFatherFieldValue(this.getPo(), nullF.getField().getName()):FieldUtil.getFieldValue(this.getPo(), nullF.getField().getName());
                String name = nullF.getColumn();
                if(value != null) {
                    this.adapter.addParameter(value);
                    gs.add(this.getQuotedItem(name), "?", "");
                    if(this.nuList != null && this.nuList.size() > 0) {
                        this.nuList.remove(nullF);
                    }
                } else if(value == null && this.updateNull) {
                    gs.add(this.getQuotedItem(name), "null", "");
                }
            }
        }

        if(this.nuList != null && this.nuList.size() > 0) {
            var2 = this.nuList.iterator();

            while(var2.hasNext()) {
                nullF = (FieldColumn)var2.next();
                if(this.fields.contains(nullF)) {
                    gs.add(this.getQuotedItem(nullF.getColumn()), "null", "");
                }
            }
        }

        this.nuList = null;
        return gs;
    }

    private String getQuotedItem(String str) {
        char[] quote = this.adapter.getQuote();
        return quote[0] + str + quote[1];
    }

    public void setUpdateNull(boolean b) {
        this.log.trace("in setUpdateNull");
        this.updateNull = b;
    }

    public DataBaseManager addParmeter(Object... parmeters) {
        Object[] var2 = parmeters;
        int var3 = parmeters.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object parmeter = var2[var4];
            this.adapter.addParameter(parmeter);
        }

        return this;
    }

    public int insert(String primarkey) throws SQLException {
        this.log.trace("in insert");
        this.removeField(primarkey);
        GetSql gs = this.getSql();
        String sql = gs.getInsert(this.tableName);
        int update = this.adapter.update(this.getConnection(), sql);
        this.tx.closeConnection();
        return update;
    }

    public int updateByKey(String key) throws SQLException {
        this.log.trace("in updateByKey");
        this.removeField(key);
        FieldColumn fieldColumn = FieldUtil.getFieldColumnByCache(FieldUtil.getField(this.getPo().getClass(), key));
        GetSql gs = this.getSql();
        Object PKvalue = FieldUtil.getFieldValue(this.getPo(), key);
        this.adapter.addParameter(PKvalue);
        gs.add(this.getQuotedItem(fieldColumn.getColumn()), "?", "where");
        String sql = gs.getUpdate(this.tableName);
        int update = this.adapter.update(this.getConnection(), sql);
        this.tx.closeConnection();
        return update;
    }

    public int autoUp(String field, String where) throws SQLException {
        this.log.trace("in autoUp");
        FieldColumn fieldColumn = FieldUtil.getFieldColumnByCache(FieldUtil.getField(this.getPo().getClass(), field));
        String sql = "update  " + this.getQuotedItem(this.tableName) + " set " + this.getQuotedItem(fieldColumn.getColumn()) + "=" + this.getQuotedItem(field) + "+1 " + where;
        int update = this.adapter.update(this.getConnection(), sql);
        this.tx.closeConnection();
        return update;
    }

    public int delete(String where) throws SQLException {
        this.log.trace("in delete");
        String sql = "delete from " + this.getQuotedItem(this.tableName) + " " + where;
        int update = this.adapter.update(this.getConnection(), sql);
        this.tx.closeConnection();
        return update;
    }

    public int deleteByKey(String key) throws SQLException {
        this.log.debug("in deleteByKey");
        FieldColumn fieldColumn = FieldUtil.getFieldColumnByCache(FieldUtil.getField(this.getPo().getClass(), key));
        String sql = "delete from " + this.getQuotedItem(this.tableName) + " where " + fieldColumn.getColumn() + " =?";
        Object PKvalue = FieldUtil.getFieldValue(this.getPo(), key);
        this.adapter.addParameter(PKvalue);
        int update = this.adapter.update(this.getConnection(), sql);
        this.tx.closeConnection();
        return update;
    }

    public T showOne(String sql, Object... parmeters) throws SQLException {
        this.log.trace("in showOne");
        this.addParmeter(parmeters);
        ResultSet rs = this.adapter.findSingle(this.getConnection(), sql);

        try {
            if(rs != null && rs.next()) {
                this.loadData(this.getPo(), rs);
            } else {
                this.po = (T)this.getPo().getClass().newInstance();
            }
        } catch (Exception var5) {
            if(var5 instanceof SQLException) {
                throw (SQLException)var5;
            }

            throw new IllegalArgumentException("loadData error!", var5);
        }

        this.tx.closeConnection();
        return this.getPo();
    }

    public T showOnebyKey(String key) throws SQLException {
        this.log.trace("in showOnebyKey");
        Object value = FieldUtil.getFieldValue(this.getPo(), key);
        FieldColumn fieldColumn = FieldUtil.getFieldColumnByCache(FieldUtil.getField(this.getPo().getClass(), key));
        this.adapter.addParameter(value);
        String sql = "select " + this.getFieldList() + " from " + this.getQuotedItem(this.tableName) + " where " + fieldColumn.getColumn() + "=?";
        return this.showOne(sql, new Object[0]);
    }

    public List<T> showAll() throws SQLException {
        this.log.trace("in showAll");
        String sql = "select " + this.getFieldList() + " from " + this.tableName;
        return this.showList(sql, new Object[0]);
    }

    public List<T> showAll(String where) throws SQLException {
        this.log.trace("in showAll (String where) ");
        String sql = "select " + this.getFieldList() + " from " + this.tableName + " " + where;
        return this.showList(sql, new Object[0]);
    }

    public int getCount(String sql) throws SQLException {
        this.log.trace("in getCount");
        int count = NumberUtils.parseInt(Integer.valueOf(this.adapter.getCount(this.getConnection(), sql)));
        this.tx.closeConnection();
        return count;
    }

    public void showByPage(String where, Pager pager) throws SQLException {
        this.log.trace("in showByPage");
        String sql = "select " + this.getFieldList() + "  from " + this.getQuotedItem(this.tableName) + " " + where;
        ResultSet rs = this.adapter.findPageList(this.getConnection(), sql, pager);
        List list = this.loadListData(this.getPo(), rs);
        this.tx.closeConnection();
        pager.setItems(Integer.valueOf(NumberUtils.parseInt(Integer.valueOf(this.adapter.getCount(this.getConnection(), sql)))));
        pager.setData(list);
        this.tx.closeConnection();
    }

    public List<T> showList(String sql, Object... parmeters) throws SQLException {
        this.addParmeter(parmeters);
        this.log.trace("in showList");
        ResultSet rs = this.adapter.findList(this.getConnection(), sql);
        List list = this.loadListData(this.getPo(), rs);
        this.tx.closeConnection();
        return list;
    }

    private List<Map> getMapList(ResultSet rs) throws SQLException {
        ArrayList list = new ArrayList();
        if(rs != null) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            this.fields = new ArrayList(columnCount);

            for(int map = 1; map <= columnCount; ++map) {
                this.fields.add(new FieldColumn((Field)null, metaData.getColumnName(map), 0, false, true));
            }

            while(rs.next()) {
                Map var7 = new HashMap();

                for(int i = 1; i <= columnCount; ++i) {
//                    System.out.println(metaData.getColumnTypeName(i)+"\t\t\t"+metaData.getColumnName(i));
                    var7.put(metaData.getColumnName(i), rs.getObject(i));
                }

                list.add(var7);
            }
        }

        return list;
    }

    public List<Map> getMapList(String sql, Object... parmeters) throws SQLException {
        this.addParmeter(parmeters);
        List mapList = this.getMapList(this.adapter.findList(this.getConnection(), sql));
        log.trace("list :{}",mapList);
        this.tx.closeConnection();
        return mapList;
    }

    public void getMapList(String sql, Pager pager, Object... parmeters) throws SQLException {
        this.addParmeter(parmeters);
        ResultSet findPageList = this.adapter.findPageList(this.getConnection(), sql, pager);
        pager.setData(this.getMapList(findPageList));
        this.tx.closeConnection();
        pager.setItems(Integer.valueOf(this.adapter.getCount(this.getConnection(), sql)));
        this.tx.closeConnection();
    }

    public List<FieldColumn> getNuList() {
        this.log.trace("in getNuList");
        return this.nuList;
    }

    public void addNullField(String field) {
        this.log.trace("in addNullField");
        if(this.firstAddField) {
            this.firstAddField = false;
            this.nuList = new ArrayList();
        }

        this.nuList.add(FieldUtil.getFieldColumnByCache(FieldUtil.getField(this.getPo().getClass(), field)));
    }

    public String getPrimaryKey() {
        return this.primaryKey;
    }
}
