package com.gaoc.chou.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.*;
import com.gaoc.chou.service.*;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.OrderByModel;
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
 * <p>
 * 合同领用表 前端控制器
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-07
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/contractReceive")
public class ContractReceiveController {

    private final IContractReceiveService contractReceiveService;

    private final ISysDeptService sysDeptService;

    private final ISysUserService sysUserService;

    private final ISysPostService sysPostService;

    private final IContractModelService contractModelService;

    private final IContractTypeService contractTypeService;

    private final ITreatyModelService treatyModelService;

    @GetMapping
    public ModelAndView receivePage() {
        return new ModelAndView("chou/contract/receive/list");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<ContractReceive> condition = new QueryWrapper<>();
        IPage<ContractReceive> page = new Page<>(current, size);

        String contractNo = MapUtil.getStr(param, "contractNo");
        if (StrUtil.isNotBlank(contractNo)) {
            condition.like("contract_no", contractNo);
        }

        String treatyNo = MapUtil.getStr(param, "treatyNo");
        if (StrUtil.isNotBlank(treatyNo)) {
            condition.like("treaty_no", treatyNo);
        }

        String deptId = MapUtil.getStr(param, "deptId");
        if (StrUtil.isNotBlank(deptId)) {
            Set<String> ids = sysDeptService.oppoIds(deptId);
            condition.in("dept_id", ids);
        }

        String empName = MapUtil.getStr(param, "empName");
        if (StrUtil.isNotBlank(empName)) {
            List<SysUser> list = sysUserService.list("name", ConditionEnum.LIKE_ANY, empName, "id");
            if (CollUtil.isEmpty(list)) {
                condition.eq("id", BaseConstant.INT_FALSE);
            } else {
                Set<String> ids = list.stream().map(SysUser::getId).collect(Collectors.toSet());
                condition.in("emp_id", ids);
            }
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        condition.orderByDesc("receive_time");
        page = contractReceiveService.page(page, condition);
        List<ContractReceive> records = page.getRecords();

        Set<String> modelIds = records.stream().map(ContractReceive::getModelId).collect(Collectors.toSet());
        List<ContractModel> models = new ArrayList<>(modelIds.size());
        if (CollUtil.isNotEmpty(modelIds)) {
            models.addAll(contractModelService.list("id", ConditionEnum.IN, modelIds, "id", "name"));
        }

        Set<String> typeIds = records.stream().map(ContractReceive::getTypeId).collect(Collectors.toSet());
        List<ContractType> types = new ArrayList<>(typeIds.size());
        if (CollUtil.isNotEmpty(typeIds)) {
            types.addAll(contractTypeService.list("id", ConditionEnum.IN, typeIds, "id", "name"));
        }

        Set<String> deptIds = records.stream().map(ContractReceive::getDeptId).collect(Collectors.toSet());
        Map<String, SysDept> oppoDept = sysDeptService.oppoDept(deptIds);

        Set<String> postIds = records.stream().map(ContractReceive::getPostId).collect(Collectors.toSet());
        List<SysPost> posts = new ArrayList<>(postIds.size());
        if (CollUtil.isNotEmpty(postIds)) {
            posts.addAll(sysPostService.list("id", ConditionEnum.IN, postIds, "id", "name"));
        }

        Set<String> empIds = records.stream().map(ContractReceive::getEmpId).collect(Collectors.toSet());
        List<SysUser> users = new ArrayList<>(empIds.size());
        if (CollUtil.isNotEmpty(empIds)) {
            users.addAll(sysUserService.list("id", ConditionEnum.IN, empIds, "id", "name"));
        }

        records.forEach(contractReceive -> {
            if (CollUtil.isNotEmpty(models)) {
                String modelId = contractReceive.getModelId();
                models.forEach(contractModel -> {
                    if (StrUtil.equals(modelId, contractModel.getId())) {
                        contractReceive.setModelName(contractModel.getName());
                    }
                });
            }

            if (CollUtil.isNotEmpty(types)) {
                String typeId = contractReceive.getTypeId();
                types.forEach(contractType -> {
                    if (StrUtil.equals(typeId, contractType.getId())) {
                        contractReceive.setTypeName(contractType.getName());
                    }
                });
            }

            if (MapUtil.isNotEmpty(oppoDept)) {
                SysDept sysDept = oppoDept.get(contractReceive.getDeptId());
                if (sysDept != null) {
                    contractReceive.setDeptName(sysDept.getName());
                }
            }

            if (CollUtil.isNotEmpty(posts)) {
                String postId = contractReceive.getPostId();
                posts.forEach(sysPost -> {
                    if (StrUtil.equals(postId, sysPost.getId())) {
                        contractReceive.setPostName(sysPost.getName());
                    }
                });
            }

            if (CollUtil.isNotEmpty(users)) {
                String empId = contractReceive.getEmpId();
                users.forEach(sysUser -> {
                    if (StrUtil.equals(empId, sysUser.getId())) {
                        contractReceive.setEmpName(sysUser.getName());
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

    @GetMapping("/saveOrUpdate")
    public ModelAndView saveOrUpdate(String id) {
        ContractReceive contractReceive = contractReceiveService.getById(id);
        if (contractReceive == null) {
            contractReceive = new ContractReceive();
        }

        ModelAndView mav = new ModelAndView("chou/contract/receive/receive");
        mav.addObject("contractReceive", contractReceive);

        List<OrderByModel> sorts = new ArrayList<>(2);
        OrderByModel s1 = new OrderByModel(ConditionEnum.ASC, new String[]{"sort"});
        OrderByModel s2 = new OrderByModel(ConditionEnum.DESC, new String[]{"id"});
        sorts.add(s1);
        sorts.add(s2);

        List<ContractModel> models = contractModelService.list("status", ConditionEnum.EQ, BaseConstant.INT_TRUE, sorts, "id", "name");
        mav.addObject("models", models);

        List<ContractType> types = contractTypeService.list("status", ConditionEnum.EQ, BaseConstant.INT_TRUE, sorts, "id", "name");
        mav.addObject("types", types);

        List<TreatyModel> treatyModels = treatyModelService.list("status", ConditionEnum.EQ, BaseConstant.INT_TRUE, sorts, "id", "name");
        mav.addObject("treatyModels", treatyModels);

        return mav;
    }

    @PostMapping("/post")
    public R listPost(String deptId) {
        if (StrUtil.isBlank(deptId)) {
            return R.fail("参数请求不合法");
        }

        QueryWrapper<SysPost> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseConstant.INT_TRUE).eq("dept_id", deptId);
        wrapper.orderByAsc("sort").orderByDesc("update_time");
        wrapper.select("id", "name");
        return R.ok(sysPostService.list(wrapper));
    }

    @PostMapping("/user")
    public R listUser(String postId) {
        if (StrUtil.isBlank(postId)) {
            return R.fail("参数请求不合法");
        }

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("entry_post_id", postId).or().eq("post_id", postId);
        wrapper.orderByAsc("job_no").orderByDesc("update_time");
        wrapper.select("id", "name");
        return R.ok(sysUserService.list(wrapper));
    }

    @PostMapping
    public R save(@RequestBody @Valid ContractReceive contractReceive) {
        List<String> sign = contractReceiveService.treatyNoSign(contractReceive.getTreatyId());
        if (CollUtil.isNotEmpty(sign)) {
            contractReceive.setTreatyNoA(sign.get(0));
            contractReceive.setTreatyNoB(sign.get(1));
        }

//        String contractNo = contractReceiveService.contractNoSign(contractReceive.getModelId());
//        contractReceive.setContractNo(contractNo);

        contractReceive.setCreateId(SessionUtil.getUserId());

        boolean save = contractReceiveService.save(contractReceive);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PutMapping
    public R edit(@RequestBody @Valid ContractReceive contractReceive) {
        if (StrUtil.isBlank(contractReceive.getId())) {
            return R.fail("请求参数不合法");
        }

        int count = contractReceiveService.count("id", ConditionEnum.EQ, contractReceive.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新页面重试");
        }

        contractReceive.setUpdateId(SessionUtil.getUserId());
        boolean update = contractReceiveService.updateById(contractReceive);
        if (update) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

}
