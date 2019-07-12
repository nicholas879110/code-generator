#set($jQuery="$.")
$(function () {
	$("#save-btn").click(function () {
		var $form = $('#add-${lowerName}-form');
		//if (!$form.valid()) {
		//	return false;
		//}
		${jQuery}ajax({
			url: getContentPath() + "/${lowerName}/add",
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
