package com.zlw.generator.db.adapter;

import com.zlw.generator.db.bean.FieldColumn;
import com.zlw.generator.db.bean.ParmeterPair;
import com.zlw.generator.db.config.ConfigCenter;
import com.zlw.generator.db.util.ISqlout;
import com.zlw.generator.util.NumberUtils;
import com.zlw.generator.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public abstract class AAdapter implements IAdapter {
    protected static final Logger log = LoggerFactory.getLogger(AAdapter.class);
    protected List<ParmeterPair> parmeters = new ArrayList(0);
    protected String sql;

    public AAdapter() {
    }

    protected Statement createStatement(Connection conn, String sql) throws SQLException {
        this.sql = sql;
        this.sqlout();
        Object stm;
        if (ConfigCenter.INSTANCE.getConfigurator().prepare()) {
            log.trace("createStatement:prepare");
            stm = conn.prepareStatement(sql);
            int index = 1;
            if (ObjectHelper.isNotEmpty(this.parmeters)) {
                Iterator var5 = this.parmeters.iterator();

                while (var5.hasNext()) {
                    ParmeterPair parmeter = (ParmeterPair) var5.next();
                    FieldColumn fieldColumn = parmeter.getFieldColumn();
                    if (fieldColumn != null && fieldColumn.getSqltype() != -1) {
                        ((PreparedStatement) stm).setObject(index++, parmeter.getParmeter(), fieldColumn.getSqltype());
                    } else {
                        ((PreparedStatement) stm).setObject(index++, parmeter.getParmeter());
                    }
                }
            }
        } else {
            log.trace("createStatement:not prepareStatement");
            stm = conn.createStatement();
        }

        return (Statement) stm;
    }

    @Override
    public void addParameter(Object o) {
        log.trace("addParameter {}", o);
        this.parmeters.add(new ParmeterPair(o, (FieldColumn) null));
    }

    @Override
    public void addParameter(Object o, FieldColumn fieldColumn) {
        log.trace("addParameter {}", o);
        this.parmeters.add(new ParmeterPair(o, fieldColumn));
    }

    @Override
    public List getParmeters() {
        log.trace("in getParmeters");
        return this.parmeters;
    }

    @Override
    public int getCount(Connection connection, String sql) throws SQLException {
        sql = "select count(*) from (" + sql + ") as count";
        Object findUnique = this.findUnique(connection, sql);
        return NumberUtils.parseInt(findUnique);
    }

    @Override
    public void sqlout() {
        if (ConfigCenter.INSTANCE.getConfigurator().sqlOut()) {
            ISqlout sqlout = ConfigCenter.INSTANCE.getSqlout();
            if (sqlout != null) {
                sqlout.sqlout(this.sql, this.parmeters);
            } else {
                log.error("can\'t find sqlout adapter,you can set it by ConfigCenter.DEFAULT_INSTANCE.setSqlout(ISqlout sqlout)");
            }
        }

    }
}
