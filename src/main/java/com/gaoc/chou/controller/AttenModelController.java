package com.gaoc.chou.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.AttenModel;
import com.gaoc.chou.service.IAttenModelService;
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

/**
 * <p>
 * 考勤模板表 前端控制器
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-15
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/attenModel")
public class AttenModelController {

    private final IAttenModelService attenModelService;

    @GetMapping
    public ModelAndView attenModelPage() {
        return new ModelAndView("chou/atten");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<AttenModel> condition = new QueryWrapper<>();
        IPage<AttenModel> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        condition.orderByDesc("month");
        page = attenModelService.page(page, condition);

        page.getRecords().forEach(attenModel -> {
            if (StrUtil.isNotBlank(attenModel.getRest())) {
                List<String> rests = StrSpliter.split(attenModel.getRest(), ',', true, true);
                attenModel.setRests(rests);
            }
            if (StrUtil.isNotBlank(attenModel.getVacation())) {
                List<JSONObject> vacations = JSON.parseArray(attenModel.getVacation(), JSONObject.class);
                attenModel.setVacations(vacations);
            }
            if (StrUtil.isNotBlank(attenModel.getWork())) {
                List<JSONObject> works = JSON.parseArray(attenModel.getWork(), JSONObject.class);
                attenModel.setWorks(works);
            }
        });

        return R.ok(page);
    }

    @PostMapping
    public R save(@RequestBody @Valid AttenModel attenModel) {
        attenModel.setStatus(BaseConstant.INT_FALSE);
        attenModel.setCreateId(SessionUtil.getUserId());
        boolean save = attenModelService.save(attenModel);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PutMapping
    public R edit(@RequestBody @Valid AttenModel attenModel) {
        if (StrUtil.isBlank(attenModel.getId())) {
            return R.fail("请求参数不合法");
        }

        int count = attenModelService.count("id", ConditionEnum.EQ, attenModel.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新重试");
        }

        attenModel.setUpdateId(SessionUtil.getUserId());
        boolean update = attenModelService.updateById(attenModel);
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
        List<AttenModel> list = attenModelService.list("id", ConditionEnum.IN, idList, "id", "status");
        if (CollUtil.isEmpty(list)) {
            return R.fail("删除数据不存在，请刷新页面重试");
        }

        boolean anyMatch = list.stream().anyMatch(attenModel -> BaseConstant.INT_TRUE == attenModel.getStatus());
        if (anyMatch) {
            return R.fail("存在已生成考勤数据的模板，无法删除");
        }

        boolean remove = attenModelService.removeByIds(idList);
        if (remove) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PostMapping("/generate")
    public R generate(String id) {
        if (StrUtil.isBlank(id)) {
            return R.fail("请求参数不合法");
        }

        return attenModelService.generate(id);
    }

}
