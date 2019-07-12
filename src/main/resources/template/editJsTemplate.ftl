#set($jQuery="$.")
$(function () {
	var ${pk.priField}=$("#update-${lowerName}-form input[name=${pk.priField}]").val();
	${jQuery}ajax({
		url: getContentPath() + "/${lowerName}/queryBy${pk.upperField}",
		type: "get",
		dataType: 'json',
		data: {${pk.priField}:${pk.priField}},
		success: function (data, textStatus, jqXHR) {
			if (data.code == MESSAGE_CODE.SUCCESS_CODE) {
				$("#update-${lowerName}-form").echodata(data.data);
			} else {
				bootBoxError(data.msg, "error！");
			}
		}
	});

	$("#save-btn").click(function () {
		var $form = $('#update-${lowerName}-form');
		${jQuery}ajax({
			url: getContentPath() + "/${lowerName}/update",
			type: $form.attr('method'),
			dataType: 'json',
			data: $form.serialize(),
			success: function (data, textStatus, jqXHR) {
				if (data.code == MESSAGE_CODE.SUCCESS_CODE) {
					bootBoxSuccess(data.msg);
				} else {
					bootBoxError(data.msg, "error！");
				}
			}
		});
	})

	$("#back-btn").click(function () {
		switchPage("/${lowerName}/list")
	})
})