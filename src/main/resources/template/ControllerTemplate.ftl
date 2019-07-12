#if(${entityPackage})
package ${entityPackage}.controller;
#else
package com.zlw.mine.performance.controller;
#end

import java.util.List;
import com.zlw.mine.commons.core.ErrorCode;
import com.zlw.mine.commons.core.ResultData;
import com.zlw.mine.commons.paging.Pager;
import com.zlw.mine.commons.paging.PagerQuery;
import com.zlw.mine.commons.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
#if(${entityPackage})
import ${entityPackage}.entity.${className};
import ${entityPackage}.service.${className}Service;
#else
import com.zlw.mine.performance.entity.${className};
import com.zlw.mine.performance.service.${className}Service;
#end

/**
 * ${descn}
 * @Description
 * @Author ${author}
 * @Date ${date}
 * @Copyright(c) PwC.普华永道
 */
@Controller
@RequestMapping("/${lowerName}") 
public class ${className}Controller extends BaseController{

	private final static String PAGE_PATH = "${moduleName}/${lowerName}";
	private final static String PAGE_LIST = PAGE_PATH + "/list";
	private final static String PAGE_ADD = PAGE_PATH + "/add";
	private final static String PAGE_EDIT = PAGE_PATH + "/edit";
	private final static String PAGE_DETAIL = PAGE_PATH + "/detail";
	
	@Autowired
	private ${className}Service ${lowerName}Service;


	/**
	 * list页面
	 * @return
	 */
	@RequestMapping(value = "list")
		public ModelAndView initView() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(PAGE_LIST);
		return mav;
	}

	/**
	 * 列表页面分页查询
	 * @param pagerQuery 分页查询对象
	 * @param ${lowerName} ${descn}
	 * @return
	 */
	@RequestMapping(value = "pager")
	@ResponseBody
	public ResultData<Pager<${className}>> pager(PagerQuery pagerQuery, ${className} ${lowerName}) {
    	try {
			ResultData<Pager<${className}>> resultData = ${lowerName}Service.pager(pagerQuery, ${lowerName});
			return resultData;
        } catch (Exception e) {
        logger.error("查询${descn}失败", e);
        return ResultData.newResultData(ErrorCode.QUERY_FAILOR, "查询${descn}失败");
        }
	}

	/**
	 * 添加页面
	 * @return
	 */
	@RequestMapping(value = "addView")
	public ModelAndView addView() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(PAGE_ADD);
		return mav;
	}

	/**
	* 新增保存操作
	* @param ${lowerName} ${descn}
	* @return
	*/
	@RequestMapping("/add")
	@ResponseBody
	public ResultData<${className}> add(${className} ${lowerName}) {
		try {
		ResultData<${className}> resultData = ${lowerName}Service.add(${lowerName});
			return resultData;
			} catch (Exception e) {
			logger.error("新增${descn}失败", e);
			return ResultData.newResultData(ErrorCode.ADD_FAILOR, "新增${descn}失败");
		}
	}


    #if(${pk})
	/**
	* 根据${pk.priField}查询
	* @param ${pk.priField} 主键${pk.priField}
	* @return
	*/
	@RequestMapping("/queryBy${pk.upperField}")
	@ResponseBody
	public ResultData<${className}> queryBy${pk.upperField}(${pk.priDataType} ${pk.priField}) {
		try {
			ResultData<${className}> resultData = ${lowerName}Service.queryById(${pk.priField});
			return resultData;
		} catch (Exception e) {
			logger.error("查询${descn}失败", e);
			return ResultData.newResultData(ErrorCode.QUERY_FAILOR, "查询${descn}失败");
		}
	}
    #end

    /**
    * 编辑页面
    * @param ${pk.priField}
    * @return
    */
    @RequestMapping(value = "editView")
    public ModelAndView editView(${pk.priDataType} ${pk.priField}) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(PAGE_EDIT);
		mav.addObject("${pk.priField}",${pk.priField});
		return mav;
    }

    /**
    * 更新操作
    * @param ${lowerName}
    * @return
    */
    @RequestMapping("/update")
    @ResponseBody
    public ResultData<${className}> update(${className} ${lowerName}) {
		try {
            ${lowerName}Service.updateBySelective(${lowerName});
			return ResultData.newResultData(ErrorCode.SUCCESS, "更新${descn}成功");
		} catch (Exception e) {
			logger.error("更新${descn}失败", e);
			return ResultData.newResultData(ErrorCode.UPDATE_FAILOR, "更新${descn}失败");
		}
    }

	/**
	 * 查询list
	 * @param ${lowerName}
     * @return
	 */
	@RequestMapping("/queryList")
	@ResponseBody
	public ResultData<List<${className}>>  queryList( ${className} ${lowerName} ) {
		try {
				ResultData<List<${className}>>  resultData= ${lowerName}Service.queryByList(${lowerName});
				return resultData;
			} catch (Exception e) {
				logger.error("查询${descn}失败", e);
				return ResultData.newResultData(ErrorCode.QUERY_FAILOR, "查询${descn}失败");
			}
	}


	/**
	 * 单个删除
	 * @param ${pk.priField}
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public ResultData<${className}> delete(${pk.priDataType} ${pk.priField}) {
		try {
            ${lowerName}Service.delete(${pk.priField});
			return ResultData.newResultData(ErrorCode.SUCCESS, "删除${descn}成功");
		} catch (Exception e) {
			logger.error("删除${descn}失败", e);
			return ResultData.newResultData(ErrorCode.DEL_FAILOR, "删除${descn}失败");
		}
    }


	/**
	* 单个删除
	* @param ${pk.priField}s
	* @return
	*/
	@RequestMapping("/bDelete")
	@ResponseBody
	public ResultData<${className}> bDelete(@RequestBody ${pk.priDataType}[] ${pk.priField}s) {
		try {
			${lowerName}Service.delete(${pk.priField}s);
			return ResultData.newResultData(ErrorCode.SUCCESS, "删除${descn}成功");
		} catch (Exception e) {
			logger.error("删除${descn}失败", e);
			return ResultData.newResultData(ErrorCode.DEL_FAILOR, "删除${descn}失败");
		}
	}

}
