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
import com.gaoc.chou.service.ISysDeptService;
import com.gaoc.chou.service.ISysPostService;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.R;
import com.gaoc.core.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 部门控制器
 * @Date: 2020/7/1 16:59
 * @Author: Gaoc
 */
@RequestMapping("/sysDept")
@RequiredArgsConstructor
@RestController
public class SysDeptController {

    private final ISysDeptService sysDeptService;

    private final ISysPostService sysPostService;

    @GetMapping
    public ModelAndView deptPage() {
        return new ModelAndView("chou/dept");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<SysDept> condition = new QueryWrapper<>();
        IPage<SysDept> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        condition.orderByDesc("sort");
        page = sysDeptService.page(page, condition);
        List<SysDept> records = page.getRecords();

        List<JSONObject> list = sysDeptService.oppoJsonList(records);
        Map<String, Object> data = new HashMap<>(4);
        data.put("current", page.getCurrent());
        data.put("size", page.getSize());
        data.put("total", page.getTotal());
        data.put("records", list);

        return R.ok(data);
    }

    @PostMapping("/tree")
    public R deptTree(String id) {
        List<SysDept> sysDepts = sysDeptService.listDeptByCompanyId(id);
        List<JSONObject> jsonList = sysDeptService.toJsonList(sysDepts);
        List<JSONObject> deptTree = sysDeptService.deptTree(jsonList, id);

        return R.ok(deptTree);
    }

    @PostMapping
    public R save(@RequestBody @Valid SysDept sysDept) {
        sysDept.setCreateId(SessionUtil.getUserId());
        boolean save = sysDeptService.save(sysDept);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PutMapping
    public R edit(@RequestBody @Valid SysDept sysDept) {
        sysDept.setUpdateId(SessionUtil.getUserId());
        boolean update = sysDeptService.updateById(sysDept);
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
        int count = sysPostService.count("dept_id", ConditionEnum.IN, idList);
        return R.ok(count);
    }

    @DeleteMapping
    public R remove(String id) {
        if (StrUtil.isBlank(id)) {
            return R.fail("非法请求参数");
        }

        int count = sysDeptService.count("id", ConditionEnum.EQ, id);
        if (count <= 0) {
            return R.fail("无效数据，请刷新页面重试");
        }

        count = sysDeptService.count("parent_id", ConditionEnum.EQ, id);
        if (count > 0) {
            return R.fail("该部门存在下级部门，无法删除");
        }

        boolean remove = sysDeptService.removeById(id);
        if (remove) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

}
