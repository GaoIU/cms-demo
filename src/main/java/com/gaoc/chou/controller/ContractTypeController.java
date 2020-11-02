package com.gaoc.chou.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.ContractType;
import com.gaoc.chou.service.IContractTypeService;
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
 * 合同类型表 前端控制器
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-23
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/contractType")
public class ContractTypeController {

    private final IContractTypeService contractTypeService;

    @GetMapping
    public ModelAndView typePage() {
        return new ModelAndView("chou/contract/type/list");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<ContractType> condition = new QueryWrapper<>();
        IPage<ContractType> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        condition.orderByAsc("sort");
        page = contractTypeService.page(page, condition);

        return R.ok(page);
    }

    @GetMapping("/saveOrUpdate")
    public ModelAndView saveOrUpdate(String id) {
        ContractType contractType = contractTypeService.getById(id);
        if (contractType == null) {
            contractType = new ContractType();
        }

        ModelAndView mav = new ModelAndView("chou/contract/type/type");
        mav.addObject("contractType", contractType);

        return mav;
    }

    @PostMapping
    public R save(@RequestBody @Valid ContractType contractType) {
        int count = contractTypeService.count("name", ConditionEnum.EQ, contractType.getName());
        if (count > 0) {
            return R.fail("该名称已被使用");
        }

        contractType.setCreateId(SessionUtil.getUserId());
        boolean save = contractTypeService.save(contractType);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PutMapping
    public R edit(@RequestBody @Valid ContractType contractType) {
        if (StrUtil.isBlank(contractType.getId())) {
            return R.fail("请求参数不合法");
        }

        int count = contractTypeService.count("id", ConditionEnum.EQ, contractType.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新页面重试");
        }

        boolean only = contractTypeService.isOnly(contractType.getId(), "name", contractType.getName());
        if (!only) {
            return R.fail("该名称已被使用");
        }

        contractType.setUpdateId(SessionUtil.getUserId());
        boolean update = contractTypeService.updateById(contractType);
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
        boolean remove = contractTypeService.removeByIds(idList);
        if (remove) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

}
