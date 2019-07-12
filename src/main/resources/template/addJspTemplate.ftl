<%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

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

        <li class="active">增加${descn}</li>
    </ul>
</div>
<div class="page-content ">
    <small class="pull-left">
        <a id="save-btn" href="javaScript:void(0);" class="btn btn-minier btn-info">保存</a>
        <a id="back-btn" href="javaScript:void(0);" class="btn btn-minier btn-info">返回</a>
    </small>
    <div class="row">
        <div class="col-xs-12">
            <form action="/${lowerName}/add" class="form-horizontal form-border" method="post" id="add-${lowerName}-form">
                #foreach($item in $!{columnDatas})
                #if($item.isPri==false)
                <div class="form-group no-margin-left no-margin-right">
                    <label class="col-sm-3 control-label col-xs-12  no-padding">${item.fieldComment}：</label>
                    <div class=" col-xs-12 col-sm-9">
                        <div class="clearfix">
                            <input class="form-control" type="text" name="${item.fieldName}">
                        </div>
                    </div>
                </div>
				#end
				#end
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/assets/js/${moduleName}/${lowerName}/add.js"/>