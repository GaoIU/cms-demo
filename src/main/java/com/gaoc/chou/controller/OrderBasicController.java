package com.gaoc.chou.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.OrderBasic;
import com.gaoc.chou.model.Outsider;
import com.gaoc.chou.model.SysDept;
import com.gaoc.chou.model.SysUser;
import com.gaoc.chou.service.IOrderBasicService;
import com.gaoc.chou.service.IOutsiderService;
import com.gaoc.chou.service.ISysDeptService;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.chou.util.EnumUtil;
import com.gaoc.chou.vo.OrderUser;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.OrderByModel;
import com.gaoc.core.model.R;
import com.gaoc.core.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户洽谈表 前端控制器
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-31
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/orderBasic")
public class OrderBasicController {

    private final IOrderBasicService orderBasicService;

    private final ISysDeptService sysDeptService;

    private final ISysUserService sysUserService;

    private final IOutsiderService outsiderService;

    @GetMapping
    public ModelAndView orderPage() {
        return new ModelAndView("chou/order/list");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<OrderBasic> condition = new QueryWrapper<>();
        IPage<OrderBasic> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        Integer customerState = MapUtil.getInt(param, "customerState");
        if (customerState != null) {
            condition.eq("customer_state", customerState);
        }

        String saleName = MapUtil.getStr(param, "saleName");
        if (StrUtil.isNotBlank(saleName)) {
            condition.like("sale_name", saleName);
        }

        Integer saleArea = MapUtil.getInt(param, "saleArea");
        if (saleArea != null) {
            condition.ge("sale_area", saleArea);
        }

        String channel = MapUtil.getStr(param, "channel");
        if (StrUtil.isNotBlank(channel)) {
            condition.like("channel", channel);
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        String beginDate = MapUtil.getStr(param, "beginDate");
        if (StrUtil.isNotBlank(beginDate)) {
            condition.ge("talk_time", beginDate);
        }

        String endDate = MapUtil.getStr(param, "endDate");
        if (StrUtil.isNotBlank(endDate)) {
            condition.le("talk_time", endDate);
        }

        String marketEmpName = MapUtil.getStr(param, "marketEmpName");
        if (StrUtil.isNotBlank(marketEmpName)) {
            List<SysUser> list = sysUserService.list("name", ConditionEnum.LIKE_ANY, marketEmpName, "id");
            if (CollUtil.isNotEmpty(list)) {
                Set<String> ids = list.stream().map(SysUser::getId).collect(Collectors.toSet());
                condition.in("market_emp", ids);
            } else {
                condition.eq("id", "0");
            }
        }

        String marketBelongName = MapUtil.getStr(param, "marketBelongName");
        if (StrUtil.isNotBlank(marketBelongName)) {
            List<SysUser> list = sysUserService.list("name", ConditionEnum.LIKE_ANY, marketBelongName, "id");
            if (CollUtil.isNotEmpty(list)) {
                Set<String> ids = list.stream().map(SysUser::getId).collect(Collectors.toSet());
                condition.in("market_belong", ids);
            } else {
                condition.eq("id", "0");
            }
        }

        String designEmpName = MapUtil.getStr(param, "designEmpName");
        if (StrUtil.isNotBlank(designEmpName)) {
            Set<String> ids = new HashSet<>();
            List<Outsider> outsiders = outsiderService.list("name", ConditionEnum.LIKE_ANY, designEmpName, "id");
            if (CollUtil.isNotEmpty(outsiders)) {
                ids.addAll(outsiders.stream().map(Outsider::getId).collect(Collectors.toSet()));
            }
            List<SysUser> list = sysUserService.list("name", ConditionEnum.LIKE_ANY, designEmpName, "id");
            if (CollUtil.isNotEmpty(list)) {
                ids.addAll(list.stream().map(SysUser::getId).collect(Collectors.toSet()));
            }

            if (CollUtil.isNotEmpty(ids)) {
                StringBuilder sb = new StringBuilder();
                ids.forEach(id -> sb.append(id).append(","));
                sb.deleteCharAt(sb.length() - 1);
                condition.apply("FIND_IN_SET(design_emp, {0})", sb.toString());
            } else {
                condition.eq("id", "0");
            }
        }

        String designBelongName = MapUtil.getStr(param, "designBelongName");
        if (StrUtil.isNotBlank(designBelongName)) {
            List<SysUser> list = sysUserService.list("name", ConditionEnum.LIKE_ANY, designBelongName, "id");
            if (CollUtil.isNotEmpty(list)) {
                Set<String> ids = list.stream().map(SysUser::getId).collect(Collectors.toSet());
                condition.in("design_belong", ids);
            } else {
                condition.eq("id", "0");
            }
        }

        String managerName = MapUtil.getStr(param, "managerName");
        if (StrUtil.isNotBlank(managerName)) {
            Set<String> ids = new HashSet<>();
            List<Outsider> outsiders = outsiderService.list("name", ConditionEnum.LIKE_ANY, managerName, "id");
            if (CollUtil.isNotEmpty(outsiders)) {
                ids.addAll(outsiders.stream().map(Outsider::getId).collect(Collectors.toSet()));
            }
            List<SysUser> list = sysUserService.list("name", ConditionEnum.LIKE_ANY, managerName, "id");
            if (CollUtil.isNotEmpty(list)) {
                ids.addAll(list.stream().map(SysUser::getId).collect(Collectors.toSet()));
            }

            if (CollUtil.isNotEmpty(ids)) {
                StringBuilder sb = new StringBuilder();
                ids.forEach(id -> sb.append(id).append(","));
                sb.deleteCharAt(sb.length() - 1);
                condition.apply("FIND_IN_SET(manager, {0})", sb.toString());
            } else {
                condition.eq("id", "0");
            }
        }

        condition.orderByDesc("talk_no").orderByAsc("talk_time");
        page = orderBasicService.page(page, condition);
        List<OrderBasic> records = page.getRecords();

        List<SysDept> sysDepts;
        // 营销中心
        Set<String> marketDeptIds = records.stream().map(OrderBasic::getMarketDept).collect(Collectors.toSet());
        // 设计中心
        Set<String> designDeptIds = records.stream().map(OrderBasic::getDesignDept).collect(Collectors.toSet());
        marketDeptIds.addAll(designDeptIds);
        if (CollUtil.isNotEmpty(marketDeptIds)) {
            sysDepts = sysDeptService.list("id", ConditionEnum.IN, marketDeptIds, "id", "name");
        } else {
            sysDepts = new ArrayList<>(0);
        }

        List<SysUser> userMarkList;
        // 营销专员
        Set<String> marketEmpIds = records.stream().map(OrderBasic::getMarketEmp).collect(Collectors.toSet());
        // 营销负责人
        Set<String> marketBelongIds = records.stream().map(OrderBasic::getMarketBelong).collect(Collectors.toSet());
        marketEmpIds.addAll(marketBelongIds);
        // 设计负责人
        Set<String> designBelongIds = records.stream().map(OrderBasic::getDesignBelong).collect(Collectors.toSet());
        marketEmpIds.addAll(designBelongIds);
        if (CollUtil.isNotEmpty(marketEmpIds)) {
            userMarkList = sysUserService.list("id", ConditionEnum.IN, marketEmpIds, "id", "name");
        } else {
            userMarkList = new ArrayList<>(0);
        }

        List<Map<String, Object>> mapList;
        // 推荐人
        Set<String> recommendIds = records.stream().map(OrderBasic::getRecommend).collect(Collectors.toSet());
        // 设计师
        Set<String> designEmpIds = records.stream().map(OrderBasic::getDesignEmp).collect(Collectors.toSet());
        // 客户经理
        Set<String> managerIds = records.stream().map(OrderBasic::getManager).collect(Collectors.toSet());
        designEmpIds.addAll(managerIds);
        designEmpIds.forEach(s -> {
            List<String> split = StrSpliter.split(s, ',', true, true);
            recommendIds.addAll(new HashSet(split));
        });
        if (CollUtil.isNotEmpty(recommendIds)) {
            mapList = sysUserService.listMaps("id", ConditionEnum.IN, recommendIds, "id", "name");
            mapList.addAll(outsiderService.listMaps("id", ConditionEnum.IN, recommendIds, "id", "name"));
        } else {
            mapList = new ArrayList<>(0);
        }

        records.forEach(ob -> {
            ob.setCustomerStateDesc(EnumUtil.obCustomerState(ob.getCustomerState()));
            if (ob.getTreatyType() != null) {
                ob.setTreatyTypeDesc(EnumUtil.obTreatyType(ob.getTreatyType()));
            }
            if (ob.getTreatyDetail() != null) {
                ob.setTreatyDetailDesc(EnumUtil.obTreatyDetail(ob.getTreatyDetail()));
            }
            if (ob.getHardcover() != null) {
                ob.setHardcoverDesc(EnumUtil.obHardcover(ob.getHardcover()));
            }
            BigDecimal salePriceMin = ob.getSalePriceMin();
            BigDecimal salePriceMax = ob.getSalePriceMax();
            String salePriceDesc = null;
            if (salePriceMin != null && salePriceMax == null) {
                salePriceDesc = salePriceMin.toString();
            } else if (salePriceMin == null && salePriceMax != null) {
                salePriceDesc = salePriceMax.toString();
            } else if (salePriceMin != null && salePriceMax != null) {
                salePriceDesc = salePriceMin.toString() + " - " + salePriceMax.toString();
            }
            ob.setSalePriceDesc(salePriceDesc);

            if (CollUtil.isNotEmpty(mapList)) {
                StringBuilder designEmpSb = new StringBuilder();
                StringBuilder managerSb = new StringBuilder();
                List<String> de = StrSpliter.split(ob.getDesignEmp(), ',', true, true);
                List<String> ma = StrSpliter.split(ob.getManager(), ',', true, true);

                mapList.forEach(m -> {
                    if (StrUtil.equals(ob.getRecommend(), MapUtil.getStr(m, "id"))) {
                        ob.setRecommendName(MapUtil.getStr(m, "name"));
                    }

                    de.forEach(s -> {
                        if (StrUtil.equals(s, MapUtil.getStr(m, "id"))) {
                            designEmpSb.append(MapUtil.getStr(m, "name")).append(",");
                        }
                    });

                    ma.forEach(s -> {
                        if (StrUtil.equals(s, MapUtil.getStr(m, "id"))) {
                            managerSb.append(MapUtil.getStr(m, "name")).append(",");
                        }
                    });
                });

                if (designEmpSb.length() > 0) {
                    designEmpSb.deleteCharAt(designEmpSb.length() - 1);
                    ob.setDesignEmpName(designEmpSb.toString());
                }
                if (managerSb.length() > 0) {
                    managerSb.deleteCharAt(managerSb.length() - 1);
                    ob.setManagerName(managerSb.toString());
                }
            }

            if (CollUtil.isNotEmpty(sysDepts)) {
                sysDepts.forEach(sysDept -> {
                    if (StrUtil.equals(ob.getMarketDept(), sysDept.getId())) {
                        ob.setMarketDeptName(sysDept.getName());
                    }
                    if (StrUtil.equals(ob.getDesignDept(), sysDept.getId())) {
                        ob.setDesignDeptName(sysDept.getName());
                    }
                });
            }

            if (CollUtil.isNotEmpty(userMarkList)) {
                userMarkList.forEach(sysUser -> {
                    if (StrUtil.equals(ob.getMarketEmp(), sysUser.getId())) {
                        ob.setMarketEmpName(sysUser.getName());
                    }
                    if (StrUtil.equals(ob.getMarketBelong(), sysUser.getId())) {
                        ob.setMarketBelongName(sysUser.getName());
                    }
                    if (StrUtil.equals(ob.getDesignBelong(), sysUser.getId())) {
                        ob.setDesignBelongName(sysUser.getName());
                    }
                });
            }
        });

        return R.ok(page);
    }

    @GetMapping("/saveOrUpdate")
    public ModelAndView saveOrUpdate(String id) {
        OrderBasic orderBasic = orderBasicService.getById(id);
        if (orderBasic == null) {
            orderBasic = new OrderBasic();
        }

        if (StrUtil.isNotBlank(orderBasic.getRecommend())) {
            SysUser sysUser = sysUserService.getOne("id", ConditionEnum.EQ, orderBasic.getRecommend(), "name");
            if (sysUser != null) {
                orderBasic.setRecommendName(sysUser.getName());
            } else {
                Outsider outsider = outsiderService.getById(orderBasic.getRecommend());
                orderBasic.setRecommendName(outsider.getName());
            }
        }

        if (StrUtil.isNotBlank(orderBasic.getMarketDept())) {
            SysDept sysDept = sysDeptService.getById(orderBasic.getMarketDept());
            orderBasic.setMarketDeptName(sysDept.getName());
        }

        if (StrUtil.isNotBlank(orderBasic.getMarketEmp())) {
            SysUser sysUser = sysUserService.getOne("id", ConditionEnum.EQ, orderBasic.getMarketEmp(), "name");
            orderBasic.setMarketEmpName(sysUser.getName());
        }

        if (StrUtil.isNotBlank(orderBasic.getMarketBelong())) {
            SysUser sysUser = sysUserService.getOne("id", ConditionEnum.EQ, orderBasic.getMarketBelong(), "name");
            orderBasic.setMarketBelongName(sysUser.getName());
        }

        if (StrUtil.isNotBlank(orderBasic.getDesignDept())) {
            SysDept sysDept = sysDeptService.getById(orderBasic.getDesignDept());
            orderBasic.setDesignDeptName(sysDept.getName());
        }

        if (StrUtil.isNotBlank(orderBasic.getDesignBelong())) {
            SysUser sysUser = sysUserService.getOne("id", ConditionEnum.EQ, orderBasic.getDesignBelong(), "name");
            orderBasic.setDesignBelongName(sysUser.getName());
        }

        if (StrUtil.isNotBlank(orderBasic.getDesignEmp())) {
            List<String> de = StrSpliter.split(orderBasic.getDesignEmp(), ',', true, true);
            StringBuilder builder = new StringBuilder();
            List<Map<String, Object>> mapList = sysUserService.listMaps("id", ConditionEnum.IN, de, "id", "name");
            List<Map<String, Object>> outders = outsiderService.listMaps("id", ConditionEnum.IN, de, "id", "name");
            mapList.addAll(outders);

            mapList.forEach(m -> {
                de.forEach(s -> {
                    if (StrUtil.equals(s, MapUtil.getStr(m, "id"))) {
                        builder.append(MapUtil.getStr(m, "name")).append(",");
                    }
                });
            });
            builder.deleteCharAt(builder.length() - 1);
            orderBasic.setDesignEmpName(builder.toString());
        }

        if (StrUtil.isNotBlank(orderBasic.getManager())) {
            List<String> ma = StrSpliter.split(orderBasic.getManager(), ',', true, true);
            StringBuilder builder = new StringBuilder();
            List<Map<String, Object>> mapList = sysUserService.listMaps("id", ConditionEnum.IN, ma, "id", "name");
            List<Map<String, Object>> outders = outsiderService.listMaps("id", ConditionEnum.IN, ma, "id", "name");
            mapList.addAll(outders);

            mapList.forEach(m -> {
                ma.forEach(s -> {
                    if (StrUtil.equals(s, MapUtil.getStr(m, "id"))) {
                        builder.append(MapUtil.getStr(m, "name")).append(",");
                    }
                });
            });
            builder.deleteCharAt(builder.length() - 1);
            orderBasic.setManagerName(builder.toString());
        }

        ModelAndView mav = new ModelAndView("chou/order/orderBasic");
        mav.addObject("orderBasic", orderBasic);

        return mav;
    }

    @PostMapping("/allUser")
    public R allUserList(@RequestBody OrderUser orderUser) {
        Integer current = orderUser.getCurrent();
        Integer size = Integer.MAX_VALUE;
        String name = orderUser.getName();
        Integer type = orderUser.getType();
        IPage<Map<String, Object>> page = sysUserService.allUser(current, size, name, type);

        Set<String> ids = orderUser.getIds();
        if (CollUtil.isNotEmpty(ids)) {
            List<Map<String, Object>> records = page.getRecords();
            ids.forEach(id -> {
                records.forEach(obj -> {
                    String str = MapUtil.getStr(obj, "id");
                    if (StrUtil.equals(id, str)) {
                        obj.put("selected", true);
                    }
                });
            });
        }

        return R.ok(page);
    }

    @PostMapping("/deptUser")
    public R searchUserByDeptId(String deptId) {
        if (StrUtil.isBlank(deptId)) {
            return R.ok(new ArrayList<>(0));
        }

        OrderByModel orderByModel = new OrderByModel(ConditionEnum.ASC, new String[]{"name"});
        List<SysUser> list = sysUserService.list("dept_id", ConditionEnum.EQ, deptId, orderByModel, "id", "name");

        return R.ok(list);
    }

    @PostMapping("/deptTree")
    public R deptTree() {
        List<SysDept> sysDepts = sysDeptService.listDeptByCompanyId(null);
        List<JSONObject> jsonList = sysDeptService.toJsonList(sysDepts);
        List<JSONObject> deptTree = sysDeptService.deptTree(jsonList, null);
        return R.ok(deptTree);
    }

    @PostMapping
    public R save(@RequestBody @Valid OrderBasic orderBasic) {
        if (StrUtil.isNotBlank(orderBasic.getMobile())) {
            if (!ReUtil.isMatch("0?(13|14|15|18|17|19)[0-9]{9}", orderBasic.getMobile())) {
                return R.fail("客户手机号码不合法");
            }
        }

        orderBasic.setStatus(BaseConstant.INT_FALSE);
        orderBasic.setCreateId(SessionUtil.getUserId());
        boolean save = orderBasicService.save(orderBasic);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PutMapping
    public R edit(@RequestBody @Valid OrderBasic orderBasic) {
        if (StrUtil.isBlank(orderBasic.getId())) {
            return R.fail("请求参数不合法");
        }

        if (StrUtil.isNotBlank(orderBasic.getMobile())) {
            if (!ReUtil.isMatch("0?(13|14|15|18|17|19)[0-9]{9}", orderBasic.getMobile())) {
                return R.fail("客户手机号码不合法");
            }
        }

        int count = orderBasicService.count("id", ConditionEnum.EQ, orderBasic.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新重试");
        }

        orderBasic.setUpdateId(SessionUtil.getUserId());
        boolean update = orderBasicService.updateById(orderBasic);
        if (update) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @GetMapping("/talk")
    public ModelAndView talkPage() {
        return new ModelAndView("chou/order/talkList");
    }

    @PostMapping("/talk")
    public R talk(String id, Integer status) {
        if (StrUtil.isBlank(id) || status == null) {
            return R.fail("请求参数不合法");
        }

        OrderBasic orderBasic = orderBasicService.getOne("id", ConditionEnum.EQ, id, "id", "status");
        if (orderBasic == null) {
            return R.fail("操作数据不存在，请刷新重试");
        }
        if (BaseConstant.INT_TRUE == orderBasic.getStatus() && BaseConstant.INT_TRUE == status) {
            return R.fail("该派单已生成洽谈数据，请勿重复操作");
        }

        return orderBasicService.talk(id, status);
    }

}
