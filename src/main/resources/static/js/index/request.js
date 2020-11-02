layui.define(['jquery', 'layer'], function (exports) {
    const $ = layui.jquery;
    const layer = layui.layer;
    const timeout = 3500;

    const request = {
        get: function (param) {
            param.type = 'GET';
            ajax(param);
        },

        post: function (param) {
            param.type = 'POST';
            ajax(param);
        },

        put: function (param) {
            param.type = 'PUT';
            ajax(param);
        },

        delete: function (param) {
            param.type = 'DELETE';
            ajax(param);
        },

        postJson: function (param) {
            param.type = 'POST';
            param.headers = param.headers || {};
            param.headers['Content-Type'] = 'application/json; charset=utf-8';
            param.data = JSON.stringify((param.data || {}));

            ajax(param);
        },

        putJson: function (param) {
            param.type = 'PUT';
            param.headers = param.headers || {};
            param.headers['Content-Type'] = 'application/json; charset=utf-8';
            param.data = JSON.stringify((param.data || {}));
            ajax(param);
        },

        deleteJson: function (param) {
            param.type = 'DELETE';
            param.headers = param.headers || {};
            param.headers['Content-Type'] = 'application/json; charset=utf-8';
            param.data = JSON.stringify((param.data || {}));
            ajax(param);
        },

        jsonQs: function (param) {
            param.headers = param.headers || {};
            param.headers['Content-Type'] = 'application/json; charset=utf-8';
            param.data = JSON.stringify((param.data || {}));
            ajax(param);
        },

        ajax: function (param) {
            ajax(param);
        }
    };

    function ajax(param) {
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
            beforeSend: function () {
                layer.load();
            },
            success: param.callback,
            error: function (xhr, textStatus, errorThrown) {
                layer.closeAll('loading');
                if (xhr.status === 500) {
                    layer.msg('系统罢工了，请联系最帅的人搞定它！', {
                        icon: 5,
                        anim: 6,
                        time: 2000,
                        end: function () {
                            window.location.href = "/500";
                        }
                    });
                } else if (xhr.status === 405) {
                    window.location.href = "/403";
                }
            }
        });
    }

    exports('request', request);
});