#if(${entityPackage})
package $!{entityPackage}.facade;
#else
package com.zlw.mine.platform.${moduleName}.facade;
#end

#if(${entityPackage})
import $!{entityPackage}.entity.${className};
#else
import com.zlw.mine.platform.${moduleName}.entity.${className};
#end
import com.zlw.mine.common.paging.Pager;
import com.zlw.mine.common.paging.PagerQuery;
import com.zlw.mine.common.facade.BaseFacade;

/**
 * ${descn}
 * @Description
 * @Author ${author}
 * @Date ${date}
 * @Copyright(c) PwC.普华永道
 */
public interface ${className}Facade extends  BaseFacade<${className}>{
    ResultData<Pager<${className}>> pager(PagerQuery pagerQuery, ${className} ${lowerName});
}
