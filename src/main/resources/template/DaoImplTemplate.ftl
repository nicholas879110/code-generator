#if(${entityPackage})
package $!{entityPackage}.dao;
#else
package com.zlw.mine.performance.dao;
#end

import com.zlw.mine.commons.dao.BaseDao;
#if(${entityPackage})
import $!{entityPackage}.entity.${className};
#else
import com.zlw.mine.performance.entity.${className};
#end

/**
 * ${descn}
 * @Description
 * @Author ${author}
 * @Date ${date}
 * @Copyright(c) PwC.普华永道
 */
public interface ${className}Dao extends BaseDao<${className}> {


}
