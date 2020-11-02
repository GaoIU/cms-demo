layui.extend({
    request: '/js/index/request'
}).use(['element', 'jquery', 'layer', 'request'], function() {
    const element = layui.element;
    const layer = layui.layer;
    const $ = layui.jquery;
    const request = layui.request;

    const homeLi = $('#menu-li').find('a:eq(0)');
    tab(homeLi);

    request.post({
        url: '/index',
        callback: function (res) {
            if (res.status !== 200) {
                layer.closeAll('loading');
                layer.msg(res.message, {icon: 5, anim: 6});
                return false;
            }

            const html = menu(res.data, '');
            $('#menu-li').after(html);
            element.render();
            layer.closeAll('loading');
        }
    });

    function menu(data, html) {
        $.each(data, function (index, resource) {
            html += '<li class="layui-nav-item">';
            let id = '';
            if (!resource.children.length) {
                id = resource.id;
            }
            html += '<a href="javascript:;" data-id="' + id + '" data-url="' + resource.url + '">';
            html += resource.name;
            html += '</a>';

            if (resource.children.length) {
                html = treeMenu(resource.children, html);
            }
            html += '</li>';
        });

        return html;
    }

    function treeMenu(children, html) {
        html += '<dl class="layui-nav-child">';
        $.each(children, function (index, resource) {
            html += '<dd>';

            if (resource.children.length) {
                html += menu(resource.children(), html);
            } else {
                html += '<a href="javascript:;" data-id="' + resource.id + '" data-url="' + resource.url + '">';
                html += resource.name;
                html += '</a>';
            }

            html += '</dd>';
        });
        html += '</dl>';

        return html;
    }

    element.on('nav(menu)', function(data) {
        tab(data);
    });

    function tab(data) {
        const id = data.attr('data-id');
        if (id == null || id == '') {
            return false;
        }

        const exist = $('li[lay-id="' + id + '"]').length;
        if (exist == 0) {
            const count = $('#tab-ul').find('li').length;
            if (count >= 7) {
                layer.msg('最多打开7个页签', {
                    icon: 5,
                    time: 1500,
                    anim: 6
                });
                return false;
            }

            const url = data.attr('data-url');
            const height = $(window).height() - 60 - 44 - 41 - 30 - 20 - 3;
            const iframe = '<iframe width="100%" height="' + height + '" frameborder="no" scrolling="auto" src="' + url + '" data-id="' + id + '"></iframe>';

            element.tabAdd('tab', {
                title: data.text(),
                content: iframe,
                id: id
            });
        }

        element.tabChange('tab', id);
    }

    $('#close-all-tab').on('click', function () {
        const lis = $('#tab-ul').find('li');
        if (lis.length) {
            $.each(lis, function (index, li) {
                const id = $(li).attr('lay-id');
                element.tabDelete('tab', id);
            })
        }
    });

    $('#info').on('click', function () {
        const anim = Math.floor(Math.random() * (6 - 0)) + 0;
        layer.open({
            type: 2,
            content: ['/info', 'yes'],
            icon: 0,
            btn: ['关闭'],
            shadeClose: true,
            title: '基本资料',
            anim: anim,
            maxmin: true,
            area: ['80%', '60%']
        });
    });

    $('#password').on('click', function () {
        let anim = Math.floor(Math.random() * (6 - 0)) + 0;
        layer.open({
            type: 2,
            content: ['/password', 'no'],
            icon: 0,
            shadeClose: true,
            title: '安全设置',
            anim: anim,
            area: ['40%', '40%']
        });
    });

    $('#signOut').on('click', function () {
        const anim = Math.floor(Math.random() * (6 - 0)) + 0;
        layer.confirm('最后问您一遍，您确认要离开？', {
            anim: anim,
            shadeClose: true,
            icon: 3,
            title: '提示',
            btn: ['去意已决', '朕再看看'],
            yes: function(index) {
                window.localStorage.removeItem('ROLE_CODE');
                layer.close(index);
                location.href = "/signOut";
            }
        });
    });
});