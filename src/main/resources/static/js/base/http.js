const http = {
	get: function(param) {
		param.type = 'GET';
		ajax(param);
	},
	post: function(param) {
		param.type = 'POST';
		ajax(param);
	},
	put: function(param) {
		param.type = 'PUT';
		ajax(param);
	},
	delete: function(param) {
		param.type = 'DELETE';
		ajax(param);
	},
	postJson: function(param) {
		param.type = 'POST';
		param.headers = param.headers || {};
		param.headers['Content-Type'] = 'application/json; charset=utf-8';
		param.data = JSON.stringify((param.data || {}));

		ajax(param);
	},
	putJson: function(param) {
		param.type = 'PUT';
		param.headers = param.headers || {};
		param.headers['Content-Type'] = 'application/json; charset=utf-8';
		param.data = JSON.stringify((param.data || {}));
		ajax(param);
	},
	deleteJson: function(param) {
		param.type = 'DELETE';
		param.headers = param.headers || {};
		param.headers['Content-Type'] = 'application/json; charset=utf-8';
		param.data = JSON.stringify((param.data || {}));
		ajax(param);
	},
	jsonQs: function(param) {
		param.headers = param.headers || {};
		param.headers['Content-Type'] = 'application/json; charset=utf-8';
		param.data = JSON.stringify((param.data || {}));
		ajax(param);
	},
	ajax: function(param) {
		ajax(param);
	}
}

function ajax(param) {
	const timeout = 3500;
	param.data = param.data || {};
	param.async = param.async || true;
	param.timeout = param.timeout || timeout;

	$.ajax({
		url: param.url,
		type: param.type,
		data: param.data,
		headers: param.headers,
		timeout: param.timeout,
		async: param.async,
		beforeSend: function() {
			param.vue.$Spin.show();
		},
		success: function(res) {
			param.vue.$Spin.hide();
			if (res.status != 200) {
				param.vue.$Message.error({
					content: res.message,
					background: true
				});
				if (param.fail) {
					param.fail();
				}
				return false;
			}

			param.callback(res);
		},
		error: function(xhr, textStatus, errorThrown) {
			param.vue.$Spin.hide();
			switch (xhr.status) {
				case 500:
					param.vue.$Message.error({
						content: '系统罢工了，请联系最帅的人搞定它！',
						onClose: function() {
							window.location.href = "/500";
						}
					});
					break;
				case 405:
					param.vue.$Message.error({
						content: '请搞清楚自己的定位！',
						onClose: function() {
							window.location.href = "/403";
						}
					});
					break;
				case 401:
					param.vue.$Message.error({
						content: '禁止白嫖，请先登录！',
						onClose: function() {
							window.location.href = "/";
						}
					});
					break;
			}
		}
	});
}
