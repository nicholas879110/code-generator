#if(${entityPackage})
package $!{entityPackage}.service;
#else
package com.zlw.mine.performance.service;
#end

#if(${entityPackage})
import $!{entityPackage}.entity.${className};
#else
import com.zlw.mine.performance.entity.${className};
#end
import com.zlw.mine.commons.core.ResultData;
import com.zlw.mine.commons.paging.Pager;
import com.zlw.mine.commons.paging.PagerQuery;
import com.zlw.mine.commons.service.BaseService;


/**
 * ${descn}
 * @Description
 * @Author ${author}
 * @Date ${date}
 * @Copyright(c) PwC.普华永道
 */
public interface ${className}Service extends  BaseService<${className}>{
    ResultData<Pager<${className}>> pager(PagerQuery pagerQuery, ${className} $!{lowerName});
}
