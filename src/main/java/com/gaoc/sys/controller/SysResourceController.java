package com.gaoc.sys.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.R;
import com.gaoc.core.util.SessionUtil;
import com.gaoc.sys.model.SysResource;
import com.gaoc.sys.service.ISysResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 后台资源控制器
 * @Date: 2020/7/3 16:59
 * @Author: Gaoc
 */
@RequestMapping("/sysResource")
@RequiredArgsConstructor
@RestController
public class SysResourceController {

    private final ISysResourceService sysResourceService;

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public ModelAndView resourcePage() {
        ModelAndView mav = new ModelAndView("sys/resource");

        boolean edit = false;
        boolean remove = false;
        String key = BaseConstant.REDIS_SYS_RESOURCE + SessionUtil.getUserId();
        List<Object> list = redisTemplate.opsForList().range(key, 0, -1);

        if (CollUtil.isNotEmpty(list)) {
            String editCode = "SYS_RESOURCE:EDIT@PUT";
            String delCode = "SYS_RESOURCE:REMOVE@DELETE";

            List<JSONObject> collect = list.stream().map(obj -> JSONObject.parseObject(obj.toString()))
                    .collect(Collectors.toList());
            edit = collect.stream().anyMatch(obj -> StrUtil.equals(editCode,
                    obj.getString("code") + "@" + obj.getString("method")));
            remove = collect.stream().anyMatch(obj -> StrUtil.equals(delCode,
                    obj.getString("code") + "@" + obj.getString("method")));
        }

        mav.addObject("edit", edit);
        mav.addObject("remove", remove);

        return mav;
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<SysResource> condition = new QueryWrapper<>();
        IPage<SysResource> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        String code = MapUtil.getStr(param, "code");
        if (StrUtil.isNotBlank(code)) {
            condition.like("code", code.toUpperCase());
        }

        Integer type = MapUtil.getInt(param, "type");
        if (type != null) {
            condition.eq("type", type);
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        condition.orderByDesc("id");
        page = sysResourceService.page(page, condition);
        List<SysResource> records = page.getRecords();

        List<JSONObject> list = sysResourceService.oppoJsonList(records);
        Map<String, Object> data = new HashMap<>(4);
        data.put("current", page.getCurrent());
        data.put("size", page.getSize());
        data.put("total", page.getTotal());
        data.put("records", list);

        return R.ok(data);
    }

    @PostMapping("/tree")
    public R tree(String id) {
        QueryWrapper<SysResource> condition = new QueryWrapper<>();
        condition.eq("type", BaseConstant.RESOURCE_TYPE_MENU);
        if (StrUtil.isNotBlank(id)) {
            condition.ne("id", id);
        }
        condition.and(i -> i.eq("parent_id", "").or().isNull("parent_id"));
        condition.orderByAsc("sort");
        List<SysResource> list = sysResourceService.list(condition);

        List<JSONObject> jsonList = sysResourceService.toJsonList(list);
        List<JSONObject> treeMenu = sysResourceService.treeMenu(jsonList, null, false, false, id);

        return R.ok(treeMenu);
    }

    @PostMapping
    public R save(@RequestBody @Valid SysResource sysResource) {
        R check = this.check(sysResource);
        if (R.isFail(check)) {
            return check;
        }

        sysResource.setCode(sysResource.getCode().toUpperCase());
        sysResource.setCreateId(SessionUtil.getUserId());
        boolean save = sysResourceService.save(sysResource);
        if (save) {
            redisTemplate.delete(BaseConstant.REDIS_MENU);
            String key = BaseConstant.REDIS_SYS_RESOURCE + SessionUtil.getUserId();
            redisTemplate.delete(key);

            return R.ok();
        }

        return R.fail("操作失败");
    }

    private R check(SysResource sysResource) {
        R checkName = this.checkName(sysResource.getId(), sysResource.getName());
        if (R.isFail(checkName)) {
            return checkName;
        }

        R checkCode = this.checkCode(sysResource.getId(), sysResource.getCode().toUpperCase());
        if (R.isFail(checkCode)) {
            return checkCode;
        }

        return R.ok();
    }

    @PostMapping("/check/name")
    public R checkName(String id, String name) {
        boolean only = sysResourceService.isOnly(id, "name", name);
        if (!only) {
            return R.fail("该资源名称已被使用");
        }

        return R.ok();
    }

    @PostMapping("/check/code")
    public R checkCode(String id, String code) {
        boolean only = sysResourceService.isOnly(id, "code", code);
        if (!only) {
            return R.fail("该资源CODE已被使用");
        }

        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody @Valid SysResource sysResource) {
        R check = this.check(sysResource);
        if (R.isFail(check)) {
            return check;
        }

        sysResource.setCode(sysResource.getCode().toUpperCase());
        sysResource.setUpdateId(SessionUtil.getUserId());
        boolean update = sysResourceService.updateById(sysResource);
        if (update) {
            redisTemplate.delete(BaseConstant.REDIS_MENU);
            String key = BaseConstant.REDIS_SYS_RESOURCE + SessionUtil.getUserId();
            redisTemplate.delete(key);

            return R.ok();
        }

        return R.fail("操作失败");
    }

    @DeleteMapping
    public R remove(String id) {
        if (StrUtil.isBlank(id)) {
            return R.fail("非法请求参数");
        }

        int count = sysResourceService.count("id", ConditionEnum.EQ, id);
        if (count <= 0) {
            return R.fail("无效数据，请刷新页面重试");
        }

        count = sysResourceService.count("parent_id", ConditionEnum.EQ, id);
        if (count > 0) {
            return R.fail("该资源存在下级资源，无法删除");
        }

        boolean remove = sysResourceService.removeResourceByIds(CollUtil.newArrayList(id));
        if (remove) {
            redisTemplate.delete(BaseConstant.REDIS_MENU);
            String key = BaseConstant.REDIS_SYS_RESOURCE + SessionUtil.getUserId();
            redisTemplate.delete(key);

            return R.ok();
        }

        return R.fail("操作失败");
    }

}
