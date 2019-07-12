#if(${entityPackage})
package $!{entityPackage}.facade.impl;
#else
package com.zlw.mine.platform.${moduleName}.facade.impl;
#end


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
#if(${entityPackage})
import ${entityPackage}.facade.${className}Facade;
#else
import com.zlw.mine.platform.${moduleName}.facade.${className}Facade;
#end
#if(${entityPackage})
import ${entityPackage}.entity.${className};
#else
import com.zlw.mine.platform.${moduleName}.entity.${className};
#end
import com.zlw.mine.common.paging.InnerPageQuery;
import com.zlw.mine.common.paging.Pager;
import com.zlw.mine.common.paging.PagerQuery;
import com.zlw.mine.mybatis.pager.util.PageQueryUtil;
import com.zlw.mine.common.facade.BaseFacadeSupport;
#if(${entityPackage})
import ${entityPackage}.service.${className}Service;
#else
import com.zlw.mine.platform.${moduleName}.service.${className}Service;
#end

import java.util.List;

/**
 * ${descn}
 * @Description
 * @Author ${author}
 * @Date ${date}
 * @Copyright(c) PwC.普华永道
 */
@Service("$!{lowerName}Facade")
public class ${className}FacadeImpl extends BaseFacadeSupport implements ${className}Facade {

	private final static Logger logger = LoggerFactory.getLogger(${className}FacadeImpl.class);

	@Autowired
    private ${className}Service $!{lowerName}Service;

    @Override
    public ResultData<Pager<${className}>> pager(PagerQuery pagerQuery,final ${className} $!{lowerName}) {
        return PageQueryUtil.queryByPage(pagerQuery, new InnerPageQuery() {
        @Override
        public ResultData<List<${className}>> innerQuery() {
                return $!{lowerName}Service.queryByList($!{lowerName});
            }
        });
    }

    @Override
    public ResultData<${className}> add(${className} $!{lowerName}) {
        return  $!{lowerName}Service.add( $!{lowerName});
    }

    @Override
    public ResultData<${className}> update(${className} $!{lowerName}) {
        return $!{lowerName}Service.update($!{lowerName});
    }

    @Override
    public ResultData<${className}> updateBySelective(${className} $!{lowerName}) {
        return $!{lowerName}Service.updateBySelective($!{lowerName});
    }

    @Override
    public ResultData<${className}> delete(String... strings) {
        return $!{lowerName}Service.delete(strings);
    }

    @Override
    public int queryByCount(${className} $!{lowerName}) {
        return $!{lowerName}Service.queryByCount($!{lowerName});
    }

    @Override
    public ResultData<List<${className}>> queryByList(${className} $!{lowerName}) {
        return $!{lowerName}Service.queryByList($!{lowerName});
    }

    @Override
    public ResultData<${className}> queryById(Object o) {
        return $!{lowerName}Service.queryById(o);
    }

    @Override
    public Page<${className}> getByPageList(BaseEntity<${className}> baseEntity, Page<${className}> page) {
        return $!{lowerName}Service.getByPageList(baseEntity,page);
    }

}
