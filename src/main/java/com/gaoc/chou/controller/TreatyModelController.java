package com.gaoc.chou.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.TreatyModel;
import com.gaoc.chou.service.ITreatyModelService;
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
 * <p>
 * 协议模板表 前端控制器
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-07
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/treatyModel")
public class TreatyModelController {

    private final ITreatyModelService treatyModelService;

    @GetMapping
    public ModelAndView treatyModelPage() {
        return new ModelAndView("chou/treaty/list");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<TreatyModel> condition = new QueryWrapper<>();
        IPage<TreatyModel> page = new Page<>(current, size);

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

        condition.orderByAsc("sort").orderByDesc("id");
        page = treatyModelService.page(page, condition);

        return R.ok(page);
    }

    @GetMapping("/saveOrUpdate")
    public ModelAndView saveOrUpdate(String id) {
        TreatyModel treatyModel = treatyModelService.getById(id);
        if (treatyModel == null) {
            treatyModel = new TreatyModel();
        }

        ModelAndView mav = new ModelAndView("chou/treaty/treaty");
        mav.addObject("treatyModel", treatyModel);

        return mav;
    }

    @PostMapping
    public R save(@RequestBody @Valid TreatyModel treatyModel) {
        int count = treatyModelService.count("code", ConditionEnum.EQ, treatyModel.getCode());
        if (count > 0) {
            return R.fail("该协议编码已被使用");
        }

        count = treatyModelService.count("code_no", ConditionEnum.EQ, treatyModel.getCodeNo());
        if (count > 0) {
            return R.fail("该协议编号已被使用");
        }

        treatyModel.setCode(treatyModel.getCode().toUpperCase());
        treatyModel.setCreateId(SessionUtil.getUserId());
        boolean save = treatyModelService.save(treatyModel);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PutMapping
    public R edit(@RequestBody @Valid TreatyModel treatyModel) {
        if (StrUtil.isBlank(treatyModel.getId())) {
            return R.fail("请求参数不合法");
        }

        int count = treatyModelService.count("id", ConditionEnum.EQ, treatyModel.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新重试");
        }

        boolean only = treatyModelService.isOnly(treatyModel.getId(), "code", treatyModel.getCode());
        if (!only) {
            return R.fail("该协议编码已被使用");
        }

        only = treatyModelService.isOnly(treatyModel.getId(), "code_no", treatyModel.getCodeNo());
        if (!only) {
            return R.fail("该协议编号已被使用");
        }

        treatyModel.setCode(treatyModel.getCode().toUpperCase());
        treatyModel.setUpdateId(SessionUtil.getUserId());
        boolean update = treatyModelService.updateById(treatyModel);
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
        boolean remove = treatyModelService.removeByIds(idList);
        if (remove) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

}
