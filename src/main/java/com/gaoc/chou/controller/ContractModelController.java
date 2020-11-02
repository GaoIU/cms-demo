package com.gaoc.chou.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.Company;
import com.gaoc.chou.model.ContractModel;
import com.gaoc.chou.service.ICompanyService;
import com.gaoc.chou.service.IContractModelService;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.R;
import com.gaoc.core.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同模板表 前端控制器
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-18
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/contractModel")
public class ContractModelController {

    private final IContractModelService contractModelService;

    private final ICompanyService companyService;

    @GetMapping
    public ModelAndView contractModelPage() {
        return new ModelAndView("chou/contract/model/list");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<ContractModel> condition = new QueryWrapper<>();
        IPage<ContractModel> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        String code = MapUtil.getStr(param, "code");
        if (StrUtil.isNotBlank(name)) {
            condition.like("code", code);
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        condition.orderByAsc("sort").orderByDesc("id");
        page = contractModelService.page(page, condition);

        List<ContractModel> records = page.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<String> ids = records.stream().map(ContractModel::getCompanyId).collect(Collectors.toSet());
            List<Company> companies = companyService.list("id", ConditionEnum.IN, ids, "id", "name");
            if (CollUtil.isNotEmpty(companies)) {
                records.forEach(contractModel -> {
                    String companyId = contractModel.getCompanyId();
                    companies.forEach(company -> {
                        if (StrUtil.equals(companyId, company.getId())) {
                            contractModel.setCompanyName(company.getName());
                        }
                    });
                });
            }
        }

        return R.ok(page);
    }

    @GetMapping("/saveOrUpdate")
    public ModelAndView saveOrUpdate(String id) {
        ContractModel contractModel = contractModelService.getById(id);
        if (contractModel == null) {
            contractModel = new ContractModel();
        } else {
            contractModel.setFileId(null);
        }
        List<Company> list = companyService.list("status", ConditionEnum.EQ, BaseConstant.INT_TRUE, "id", "name");

        ModelAndView mav = new ModelAndView("chou/contract/model/model");
        mav.addObject("contractModel", contractModel);
        mav.addObject("companys", list);

        return mav;
    }

    @PostMapping
    public R save(@RequestBody @Valid ContractModel contractModel) {
        contractModel.setCreateId(SessionUtil.getUserId());
        return contractModelService.saveContractModel(contractModel);
    }

    @PutMapping
    public R edit(@RequestBody @Valid ContractModel contractModel) {
        if (StrUtil.isBlank(contractModel.getId())) {
            return R.fail("请求参数不合法");
        }

        int count = contractModelService.count("id", ConditionEnum.EQ, contractModel.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新页面重试");
        }

        contractModel.setUpdateId(SessionUtil.getUserId());
        return contractModelService.updateContractModel(contractModel);
    }

    @DeleteMapping
    public R remove(@RequestBody String[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            return R.fail("非法请求参数");
        }
        List<String> idList = CollUtil.newArrayList(ids);

        return contractModelService.removeContractModel(idList);
    }

}
