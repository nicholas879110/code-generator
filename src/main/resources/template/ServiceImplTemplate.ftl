#if(${entityPackage})
package $!{entityPackage}.service.impl;
#else
package com.zlw.mine.performance.service.impl;
#end


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zlw.mine.commons.core.ResultData;
import com.zlw.mine.commons.paging.Pager;
import com.zlw.mine.commons.paging.PagerQuery;
#if(${entityPackage})
import $!{entityPackage}.dao.${className}Dao;
#else
import com.zlw.mine.performance.dao.${className}Dao;
#end
#if(${entityPackage})
import $!{entityPackage}.entity.${className};
#else
import com.zlw.mine.performance.entity.${className};
#end
import com.zlw.mine.commons.service.BaseServiceSupport;
#if(${entityPackage})
import $!{entityPackage}.service.${className}Service;
#else
import com.zlw.mine.performance.service.${className}Service;
#end

import java.util.List;

/**
 * ${descn}
 * @Description
 * @Author ${author}
 * @Date ${date}
 * @Copyright(c) PwC.普华永道
 */
@Service("$!{lowerName}Service")
public class ${className}ServiceImpl extends BaseServiceSupport<${className}> implements ${className}Service {

	private final static Logger logger = LoggerFactory.getLogger(${className}ServiceImpl.class);
	
	@Autowired
    private ${className}Dao $!{lowerName}Dao;
    
    @Override
	public ${className}Dao getDao() {
		return $!{lowerName}Dao;
	}

@Override
public ResultData<Pager<${className}>> pager(PagerQuery pagerQuery, ${className} $!{lowerName}) {
        PageHelper.startPage(pagerQuery.getiDisplayStart(), pagerQuery.getiDisplayLength(), true);
        List<${className}> result = $!{lowerName}Dao.queryByList($!{lowerName});
        PageInfo<${className}> pageInfo = new PageInfo<${className}>(result);
        Pager<${className}> pager = new Pager<${className}>();
        pager.setAaData(pageInfo.getList());
        if ((pager.getAaData() == null || pager.getAaData().size() == 0) && result.size() > 0) {
            pager.setAaData(result);
        }
        pager.setiTotalDisplayRecords(pageInfo.getTotal());
        pager.setiTotalRecords(pageInfo.getTotal());
        return ResultData.newResultData(pager);
     }
	
}
