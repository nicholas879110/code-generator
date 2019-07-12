package com.zlw.generator.db.util;

import com.zlw.generator.db.bean.ParmeterPair;

import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public interface ISqlout {

    void sqlout(String var1, List<ParmeterPair> var2);

    void setSqlout(boolean var1);

    boolean isSqlout();
}
