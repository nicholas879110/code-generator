#set($jQuery="$.")
jQuery(function ($) {
    $("#${lowerName}-list").dataTable({
        "sAjaxSource": getContentPath() + "/${lowerName}/pager",
        "bAutoWidth":false,
        "aoColumns": [
            #if($!{pk})
            {
            "sWidth": "5%",
            "bSortable": false,
            "mData": "${pk.priField}",
            "sTitle": "<label class='center'><input type=\"checkbox\" class=\"ace\"/><span class=\"lbl\"></span></label>",
            "sClass": "center"
            },
            #end
            #foreach($item in $!{columnDatas})
            {"sWidth": "5%", "sTitle": "${item.fieldComment}", "sClass": "center", "mData": "${item.fieldName}", "bSortable": false},
            #end
            {"sWidth": "5%", "sTitle": "操作", "sClass": "center", "mData": "$!{pk.priField}", "bSortable": false}
        ],
        "aaSorting": [[1, 'asc']],
        "aLengthMenu": [10, 20, 30],
        "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            var pk = aData["${pk.priField}"];
            $('td:eq(0)', nRow).html('<div class="center"><input type="checkbox" class="ace" name="${pk.priField}" value="' + pk + '"/><span style="width:0px;" class="lbl"></span></div>');
            var operation = '<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'
            operation += '<a href="javascript:void(0)" onclick="update${className}(\'' + pk + '\');" class="blue" title="编辑">编辑</a>'
            operation += '<a href="javascript:void(0)" onclick="delete${className}(\'' + pk + '\');" class="blue" title="删除">删除</a>'
            operation += "</div>";
            #set($i=$!{columnDatas.size()})
            #set($var=$i + 1)
            $('td:eq(${var})', nRow).html(operation);
        },
        "fnServerParams": function (aoData) {
            var queryParameters = $("#search-${lowerName}-form").serializeArray();
            $(queryParameters).each(function (i, v) {
                if(!${jQuery}isEmpty(v.value)){
                    aoData.push(v);
                }
            });
        }
    });


    /**
    *${descn}增加
    */
    $("#add-btn").click(function () {
        switchPage("/${lowerName}/addView");
    });

    /**
    *${descn}批量删除
    */
    $("#bdelete-btn").click(function () {
        var checkedAr = $('#${lowerName}-list').find("[name='${pk.priField}']:checked");
        if (checkedAr.size() == 0) {
            bootBoxWarning("请至少选中一行数据！");
            return;
        }
        bootBoxConfirm("确定是否删除这些数据？", function (result) {
            if (result) {
                if (checkedAr) {
                    var ids = [];
                    ${jQuery}each(checkedAr, function (index, obj) {
                        var sData = $('#${lowerName}-list').dataTable().fnGetData($(obj).parents('#${lowerName}-list tr').get(0));
                        ids.push(sData.${pk.priField});
                    });
                    ${jQuery}ajax({
                        url: getContentPath() + '/${lowerName}/bDelete',
                        type: 'post',
                        dataType:"json",
                        contentType:"application/json",
                        data: JSON.stringify(ids),
                        success: function (data, textStatus, jqXHR) {
                            if (data.code == MESSAGE_CODE.SUCCESS_CODE) {
                                $('#${lowerName}-list').dataTable().fnClearTable();
                                bootBoxSuccess(data.msg)
                             } else {
                                bootBoxError(data.msg);
                             }
                        }
                    });
                }
            }
        });
    });


    /**
     *${descn}查询
     */
    $("#btn-search").click(function () {
        var oSettings = $('#${lowerName}-list').dataTable().fnSettings();
        oSettings._iDisplayStart = 0;
        $('#${lowerName}-list').dataTable().fnClearTable();
    })

    $("#btn-clear").click(function () {
        $("#search-${lowerName}-form").resetForm();
    })
})


/**
*${descn}更新
* @param ${pk.priField}
*/
function update${className}(${pk.priField}) {
    switchPage("/${lowerName}/editView", {${pk.priField}: ${pk.priField}});
}

/**
*${descn}删除
* @param ${pk.priField}
*/
function delete${className}(${pk.priField}) {
    bootBoxConfirm("是否删除${descn}?", function (result) {
        if (result) {
            ${jQuery}ajax({
                url: getContentPath() + "/${lowerName}/delete",
                type: 'post',
                dataType:"json",
                data: {${pk.priField}:${pk.priField}},
                success: function (data, textStatus, jqXHR) {
                    if (data.code == MESSAGE_CODE.SUCCESS_CODE) {
                        $('#${lowerName}-list').dataTable().fnClearTable();
                    } else {
                        bootBoxError(data.msg)
                    }
                }
            });
        }
    })
}