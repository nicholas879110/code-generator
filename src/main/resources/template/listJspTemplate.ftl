<%--
Description:${descn}
Auhhor: ${author}
Date: ${date}
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="breadcrumbs " id="breadcrumbs">
    <script type="text/javascript">
        try {
            ace.settings.check('breadcrumbs', 'fixed')
        } catch (e) {
        }
    </script>

    <ul class="breadcrumb">
        <li>
            <i class="ace-icon fa fa-home home-icon"></i>
            <a href="#">首页</a>
        </li>
        <li>
            <a href="#">系统管理</a>
        </li>
        <li class="active">${descn}管理</li>
    </ul>
</div>
<div class="page-content ">
    <div class="row">
        <div class="col-xs-12">
            <form id="search-${lowerName}-form" class="search-box form-horizontal" action="" method="post">
                <div class="search-body">
                    #set ($i=0)
                    #set ($pris=0)
                    #set ($size=$!{columnDatas.size()})
                    #foreach($item in $!{columnDatas})
                    #if($item.isPri==false)
                    #set($i=$i+1)
                    #set($var=$i%3)
					#if($var==1)
                    <div class="search-row">
                    #end
						<div class="col-xs-4">
							<label class="" for="${item.fieldName}">${item.fieldComment}：</label>
							<input id="${item.fieldName}" type="text" class="input-medium" placeholder="${item.fieldComment}"
								   name="${item.fieldName}">
						</div>
                    #if($var==0 || $i== ${size} - $pris)
                    </div>
                    #end
                    #else
                    #set($pris=$pris+1)
                    #end
                    #end
                    <div class="search-row">
                        <div class="col-xs-4">
                            <a id="btn-search" class="btn btn-white btn-sm btn-primary" href="javascript:void(0)">查询</a>
                            <a id="btn-clear" class="btn btn-white btn-sm btn-primary" href="javascript:void(0)">清空</a>
                        </div>
                    </div>
                </div>
            </form>
            <div class="hr-dotted hr"></div>
            <small class="pull-left">
                <button class="btn btn-minier btn-info" type="button" id="add-btn">增加</button>
                <button class="btn btn-minier btn-info" type="button" id="bdelete-btn">批量删除</button>
            </small>
            <table id="${lowerName}-list" class="table table-striped table-bordered table-hover"></table>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/assets/js/${moduleName}/${lowerName}/list.js"/>