package com.gaoc.chou.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.SysDept;
import com.gaoc.chou.model.SysPost;
import com.gaoc.chou.service.ISysDeptService;
import com.gaoc.chou.service.ISysPostService;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.R;
import com.gaoc.core.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 岗位控制器
 * @Date: 2020/7/1 17:14
 * @Author: Gaoc
 */
@RequestMapping("/sysPost")
@RequiredArgsConstructor
@RestController
public class SysPostController {

    private final ISysPostService sysPostService;

    private final ISysDeptService sysDeptService;

    private final ISysUserService sysUserService;

    @GetMapping
    public ModelAndView postPage() {
        return new ModelAndView("chou/post");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<SysPost> condition = new QueryWrapper<>();
        IPage<SysPost> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        condition.orderByAsc("sort");
        page = sysPostService.page(page, condition);
        List<SysPost> records = page.getRecords();

        Set<String> deptIds = records.stream().map(SysPost::getDeptId).collect(Collectors.toSet());
        List<SysDept> deptList = new ArrayList<>(deptIds.size());
        if (CollUtil.isNotEmpty(deptIds)) {
            List<SysDept> sysDepts = sysDeptService.list("id", ConditionEnum.IN, deptIds, "id", "name");
            deptList.addAll(sysDepts);
        }

        records.forEach(sysPost -> {
            if (CollUtil.isNotEmpty(deptList)) {
                deptList.forEach(sysDept -> {
                    if (StrUtil.equals(sysPost.getDeptId(), sysDept.getId())) {
                        sysPost.setDeptName(sysDept.getName());
                    }
                });
            }
        });

        return R.ok(page);
    }

    @PostMapping("/dept/tree")
    public R deptTree() {
        List<SysDept> sysDepts = sysDeptService.listDeptByCompanyId(null);
        List<JSONObject> jsonList = sysDeptService.toJsonList(sysDepts);
        List<JSONObject> deptTree = sysDeptService.deptTree(jsonList, null);

        return R.ok(deptTree);
    }

    @PostMapping
    public R save(@RequestBody @Valid SysPost sysPost) {
        sysPost.setCreateId(SessionUtil.getUserId());
        boolean save = sysPostService.save(sysPost);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PutMapping
    public R edit(@RequestBody @Valid SysPost sysPost) {
        sysPost.setUpdateId(SessionUtil.getUserId());
        boolean update = sysPostService.updateById(sysPost);
        if (update) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PostMapping("/check")
    public R check(@RequestBody String[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            return R.fail("非法请求参数");
        }

        List<String> idList = CollUtil.newArrayList(ids);
        int count = sysUserService.count("post_id", ConditionEnum.IN, idList);
        return R.ok(count);
    }

    @DeleteMapping
    public R remove(@RequestBody String[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            return R.fail("非法请求参数");
        }

        List<String> idList = CollUtil.newArrayList(ids);
        boolean remove = sysPostService.removePost(idList);
        if (remove) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

}
