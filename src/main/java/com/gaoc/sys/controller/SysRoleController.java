package com.gaoc.sys.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.util.EnumUtil;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.R;
import com.gaoc.core.util.SessionUtil;
import com.gaoc.sys.model.SysResource;
import com.gaoc.sys.model.SysRole;
import com.gaoc.sys.model.SysRoleResource;
import com.gaoc.sys.service.ISysResourceService;
import com.gaoc.sys.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 后台角色控制器
 * @Date: 2020/7/2 13:08
 * @Author: Gaoc
 */
@RequestMapping("/sysRole")
@RequiredArgsConstructor
@RestController
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    private final ISysResourceService sysResourceService;

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public ModelAndView rolePage() {
        return new ModelAndView("sys/role");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<SysRole> condition = new QueryWrapper<>();
        IPage<SysRole> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        String code = MapUtil.getStr(param, "code");
        if (StrUtil.isNotBlank(code)) {
            condition.like("code", code.toUpperCase());
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        condition.orderByDesc("id");
        page = sysRoleService.page(page, condition);

        List<SysRole> records = page.getRecords();
        records.forEach(sysRole -> {
            sysRole.setStatusDesc(EnumUtil.intStatus(sysRole.getStatus()));
        });

        return R.ok(page);
    }

    @PostMapping("/resource/tree")
    public R resourceTree(String id) {
        QueryWrapper<SysResource> condition = new QueryWrapper<>();
        condition.eq("type", BaseConstant.RESOURCE_TYPE_MENU);
        condition.and(i -> i.eq("parent_id", "").or().isNull("parent_id"));
        condition.orderByAsc("sort");
        List<SysResource> list = sysResourceService.list(condition);

        List<JSONObject> jsonList = sysResourceService.toJsonList(list);
        List<JSONObject> roleTree = sysResourceService.roleTree(jsonList);

        List<String> resourceIds;
        if (StrUtil.isBlank(id)) {
            resourceIds = new ArrayList<>(0);
        } else {
            Set<String> roleIds = new HashSet<>(1);
            roleIds.add(id);
            List<SysResource> resources = sysResourceService.listBySysRoleIds(roleIds);
            resourceIds = resources.stream().filter(sr -> BaseConstant.RESOURCE_TYPE_BTN == sr.getType())
                    .map(SysResource::getId).collect(Collectors.toList());
        }

        Map<String, Object> data = new HashMap<>(2);
        data.put("roleTree", roleTree);
        data.put("resourceIds", resourceIds);

        return R.ok(data);
    }

    @PostMapping
    public R save(@RequestBody @Valid SysRole sysRole) {
        R checkCode = this.checkCode(null, sysRole.getCode().toUpperCase());
        if (R.isFail(checkCode)) {
            return checkCode;
        }

        sysRole.setCode(sysRole.getCode().toUpperCase());
        sysRole.setCreateId(SessionUtil.getUserId());
        List<SysRoleResource> srr = this.getSrr(sysRole.getId(), sysRole.getSysResourceIds());

        boolean save = sysRoleService.saveSysRole(sysRole, srr);
        if (save) {
            redisTemplate.delete(BaseConstant.REDIS_MENU);
            String key = BaseConstant.REDIS_SYS_RESOURCE + SessionUtil.getUserId();
            redisTemplate.delete(key);

            return R.ok();
        }

        return R.fail("操作失败");
    }

    private List<SysRoleResource> getSrr(String id, List<String> sysResourceIds) {
        List<SysRoleResource> sysRoleResourceList = null;
        if (CollUtil.isNotEmpty(sysResourceIds)) {
            sysRoleResourceList = sysResourceIds.stream().map(sysResourceId -> new SysRoleResource(id, sysResourceId))
                    .collect(Collectors.toList());

            QueryWrapper<SysResource> condition = new QueryWrapper<>();
            condition.eq("status", BaseConstant.INT_TRUE).eq("type", BaseConstant.RESOURCE_TYPE_FUN);
            condition.in("parent_id", sysResourceIds).select("id").groupBy("id");
            List<SysResource> list = sysResourceService.list(condition);

            if (CollUtil.isNotEmpty(list)) {
                List<SysRoleResource> collect = list.stream().map(sr -> new SysRoleResource(id, sr.getId()))
                        .collect(Collectors.toList());
                sysRoleResourceList.addAll(collect);
            }
        }

        return sysRoleResourceList;
    }

    @PutMapping
    public R edit(@RequestBody @Valid SysRole sysRole) {
        String id = sysRole.getId();
        if (StrUtil.isBlank(id)) {
            return R.fail("请求参数不合法");
        }

        int count = sysRoleService.count("id", ConditionEnum.EQ, sysRole.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新重试");
        }

        R checkCode = this.checkCode(id, sysRole.getCode().toUpperCase());
        if (R.isFail(checkCode)) {
            return checkCode;
        }

        if (BaseConstant.DEFAULT_ROLE_CODE.equalsIgnoreCase(sysRole.getCode())) {
            return R.fail("超级管理员禁止任何操作");
        }

        SysRole role = sysRoleService.getOne("id", ConditionEnum.EQ, id, "code");
        if (BaseConstant.DEFAULT_ROLE_CODE.equalsIgnoreCase(role.getCode())) {
            return R.fail("超级管理员禁止任何操作");
        }

        sysRole.setCode(sysRole.getCode().toUpperCase());
        sysRole.setCreateId(SessionUtil.getUserId());
        List<SysRoleResource> srr = this.getSrr(sysRole.getId(), sysRole.getSysResourceIds());

        boolean update = sysRoleService.editSysRole(sysRole, srr);
        if (update) {
            redisTemplate.delete(BaseConstant.REDIS_MENU);
            String key = BaseConstant.REDIS_SYS_RESOURCE + SessionUtil.getUserId();
            redisTemplate.delete(key);

            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PostMapping("/check/code")
    public R checkCode(String id, String code) {
        boolean only = sysRoleService.isOnly(id, "code", code);
        if (!only) {
            return R.fail("该角色CODE已被使用");
        }

        return R.ok();
    }

    @DeleteMapping
    public R remove(@RequestBody String[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            return R.fail("非法请求参数");
        }

        List<String> idList = CollUtil.newArrayList(ids);

        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids).eq("code", BaseConstant.DEFAULT_ROLE_CODE);
        int count = sysRoleService.count(wrapper);
        if (count > 0) {
            return R.fail("超级管理员禁止任何操作");
        }

        boolean remove = sysRoleService.removeRoleByIds(idList);
        if (remove) {
            redisTemplate.delete(BaseConstant.REDIS_MENU);
            String key = BaseConstant.REDIS_SYS_RESOURCE + SessionUtil.getUserId();
            redisTemplate.delete(key);

            return R.ok();
        }

        return R.fail("操作失败");
    }

}
