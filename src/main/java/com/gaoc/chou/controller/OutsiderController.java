package com.gaoc.chou.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.Outsider;
import com.gaoc.chou.service.IOutsiderService;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.R;
import com.gaoc.core.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Description: 外部人员控制器
 * @Date: 2020/7/23 11:16
 * @Author: Gaoc
 */
@RequestMapping("/outsider")
@RequiredArgsConstructor
@RestController
public class OutsiderController {

    private final IOutsiderService outsiderService;

    @GetMapping
    public ModelAndView outsiderPage() {
        return new ModelAndView("chou/outsider");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<Outsider> condition = new QueryWrapper<>();
        IPage<Outsider> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        String mobile = MapUtil.getStr(param, "mobile");
        if (StrUtil.isNotBlank(mobile)) {
            condition.like("mobile", mobile);
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        condition.orderByDesc("id");
        page = outsiderService.page(page, condition);

        return R.ok(page);
    }

    @PostMapping
    public R save(@RequestBody @Valid Outsider outsider) {
        int count = outsiderService.count("mobile", ConditionEnum.EQ, outsider.getMobile());
        if (count > 0) {
            return R.fail("该手机号已被使用");
        }

        outsider.setCreateId(SessionUtil.getUserId());
        boolean save = outsiderService.save(outsider);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PutMapping
    public R edit(@RequestBody @Valid Outsider outsider) {
        if (StrUtil.isBlank(outsider.getId())) {
            return R.fail("请求参数不合法");
        }

        int count = outsiderService.count("id", ConditionEnum.EQ, outsider.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新重试");
        }

        boolean only = outsiderService.isOnly(outsider.getId(), "mobile", outsider.getMobile());
        if (!only) {
            return R.fail("该手机号已被使用");
        }

        outsider.setUpdateId(SessionUtil.getUserId());
        boolean update = outsiderService.updateById(outsider);
        if (update) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @DeleteMapping
    public R remove(@RequestBody String[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            return R.fail("非法请求参数");
        }

        List<String> idList = CollUtil.newArrayList(ids);
        boolean remove = outsiderService.removeByIds(idList);
        if (remove) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

}
