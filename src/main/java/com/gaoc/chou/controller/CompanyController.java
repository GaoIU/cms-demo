package com.gaoc.chou.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.Company;
import com.gaoc.chou.service.ICompanyService;
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
 * @Description: 公司控制器
 * @Date: 2020/7/1 14:38
 * @Author: Gaoc
 */
@RequestMapping("/company")
@RequiredArgsConstructor
@RestController
public class CompanyController {

    private final ICompanyService companyService;

    @GetMapping
    public ModelAndView companyPage() {
        return new ModelAndView("chou/company");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<Company> condition = new QueryWrapper<>();
        IPage<Company> page = new Page<>(current, size);

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
        page = companyService.page(page, condition);

        page.getRecords().forEach(company -> {
            if (StrUtil.isNotBlank(company.getTouch())) {
                List<JSONObject> touchs = JSON.parseArray(company.getTouch(), JSONObject.class);
                company.setTouchs(touchs);
            }
        });

        return R.ok(page);
    }

    @PostMapping
    public R save(@RequestBody @Valid Company company) {
        R r = this.checkParam(company);
        if (R.isFail(r)) {
            return r;
        }

        company.setCode(company.getCode().toUpperCase());
        company.setCreateId(SessionUtil.getUserId());
        boolean save = companyService.save(company);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    private R checkParam(Company company) {
        if (StrUtil.isNotBlank(company.getTouch())) {
            R checkTouch = this.checkTouch(company.getTouch());
            if (R.isFail(checkTouch)) {
                return checkTouch;
            }
        }

        R checkCode = this.checkCode(company.getId(), company.getCode());
        if (R.isFail(checkCode)) {
            return checkCode;
        }

        R checkDutyNo = this.checkDutyNo(company.getId(), company.getDutyNo());
        if (R.isFail(checkDutyNo)) {
            return checkDutyNo;
        }

        R checkBankNo = this.checkBankNo(company.getId(), company.getBankNo());
        if (R.isFail(checkBankNo)) {
            return checkBankNo;
        }

        return R.ok();
    }

    @PutMapping
    public R edit(@RequestBody @Valid Company company) {
        if (StrUtil.isBlank(company.getId())) {
            return R.fail("请求参数不合法");
        }

        R r = this.checkParam(company);
        if (R.isFail(r)) {
            return r;
        }

        int count = companyService.count("id", ConditionEnum.EQ, company.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新重试");
        }

        company.setCode(company.getCode().toUpperCase());
        company.setUpdateId(SessionUtil.getUserId());
        boolean update = companyService.updateById(company);
        if (update) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    private R checkTouch(String touch) {
        JSONArray jsonArray = null;
        try {
            jsonArray = JSONArray.parseArray(touch);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("联系人格式不合法");
        }

        if (jsonArray != null && jsonArray.size() > 0) {
            for (Object o : jsonArray) {
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(o));
                String name = jsonObject.getString("name");
                String mobile = jsonObject.getString("mobile");
                if (StrUtil.isBlank(name) || StrUtil.isBlank(mobile)) {
                    return R.fail("联系人格式有误，请补全联系人姓名或联系人手机号码");
                }
                if (!ReUtil.isMatch("0?(13|14|15|18|17|19)[0-9]{9}", mobile)) {
                    return R.fail("存在不合法的联系人手机号码：" + mobile);
                }
            }
        }

        return R.ok();
    }

    @PostMapping("/check/code")
    public R checkCode(String id, String code) {
        boolean only = companyService.isOnly(id, "code", code);
        if (!only) {
            return R.fail("该公司CODE已被使用");
        }

        return R.ok();
    }

    @PostMapping("/check/dutyNo")
    public R checkDutyNo(String id, String dutyNo) {
        boolean only = companyService.isOnly(id, "duty_no", dutyNo);
        if (!only) {
            return R.fail("该公司税号已存在");
        }

        return R.ok();
    }

    @PostMapping("/check/bankNo")
    public R checkBankNo(String id, String bankNo) {
        boolean only = companyService.isOnly(id, "bank_no", bankNo);
        if (!only) {
            return R.fail("该银行账号已存在");
        }

        return R.ok();
    }

    @DeleteMapping
    public R remove(@RequestBody String[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            return R.fail("非法请求参数");
        }

        List<String> idList = CollUtil.newArrayList(ids);
        boolean remove = companyService.removeByIds(idList);
        if (remove) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

}
