package com.zlw.generator.db.util;

import com.zlw.generator.db.bean.ParmeterPair;
import com.zlw.generator.db.config.ConfigCenter;
import com.zlw.generator.util.ObjectHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class SimpleSqlout implements ISqlout {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Boolean needout;

    public SimpleSqlout() {
    }

    @Override
    public void sqlout(String sql, List<ParmeterPair> parmeters) {
        if (this.isSqlout()) {
            if (ObjectHelper.isNotEmpty(parmeters)) {
                try {
                    Iterator var3 = parmeters.iterator();

                    while (var3.hasNext()) {
                        ParmeterPair parmeter = (ParmeterPair) var3.next();
                        Object o = parmeter.getParmeter();
                        if (o != null) {
                            if (o instanceof Number) {
                                sql = sql.replaceFirst("[?]", o + "");
                            } else if (o instanceof Date) {
                                sql = sql.replaceFirst("[?]", "\'" + dateFormat.format((Date) o) + "\'");
                            } else {
                                sql = sql.replaceFirst("[?]", "\'" + o + "\'");
                            }
                        } else {
                            sql = sql.replaceFirst("[?]", "null");
                        }
                    }
                } catch (Exception var6) {
                    ;
                }
            }

            System.out.println("sqlout:  " + sql);
        }

    }

    @Override
    public void setSqlout(boolean needout) {
        this.needout = Boolean.valueOf(needout);
    }

    @Override
    public boolean isSqlout() {
        return (this.needout == null ? (this.needout = Boolean.valueOf(ConfigCenter.INSTANCE.getConfigurator().sqlOut())) : this.needout).booleanValue();
    }
}
