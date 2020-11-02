package com.gaoc.chou.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.SysUser;
import com.gaoc.chou.model.TalkDetail;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.chou.service.ITalkDetailService;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.exception.MyException;
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
 * 洽谈明细表 前端控制器
 * </p>
 *
 * @author Gaoc
 * @since 2020-08-13
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/talkDetail")
public class TalkDetailController {

    private final ITalkDetailService talkDetailService;

    private final ISysUserService sysUserService;

    @GetMapping("/page")
    public ModelAndView talkDetailPage(String talkId) {
        if (StrUtil.isBlank(talkId)) {
            throw new MyException(400, "非法请求参数");
        }

        ModelAndView mav = new ModelAndView("chou/talk/list");
        mav.addObject("talkId", talkId);
        return mav;
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<TalkDetail> condition = new QueryWrapper<>();
        IPage<TalkDetail> page = new Page<>(current, size);

        condition.eq("talk_id", MapUtil.getStr(param, "talkId"));
        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.like("name", name);
        }

        condition.orderByDesc("rate_time", "id");
        page = talkDetailService.page(page, condition);

        List<TalkDetail> records = page.getRecords();
        Set<String> userIds = records.stream().map(TalkDetail::getUserId).collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(userIds)) {
            List<SysUser> list = sysUserService.list("id", ConditionEnum.IN, userIds, "id", "name");
            if (CollUtil.isNotEmpty(list)) {
                records.forEach(talkDetail -> {
                    list.forEach(u -> {
                        if (StrUtil.equals(talkDetail.getUserId(), u.getId())) {
                            talkDetail.setName(u.getName());
                        }
                    });
                });
            }
        }

        return R.ok(page);
    }

    @GetMapping("/saveOrUpdate")
    public ModelAndView saveOrUpdate(String id, String talkId) {
        if (StrUtil.isBlank(talkId)) {
            throw new MyException(400, "非法请求参数");
        }

        TalkDetail talkDetail = talkDetailService.getById(id);
        if (talkDetail == null) {
            talkDetail = new TalkDetail();
            talkDetail.setTalkId(talkId);
        }

        if (StrUtil.isNotBlank(talkDetail.getUserId())) {
            SysUser sysUser = sysUserService.getOne("id", ConditionEnum.EQ, talkDetail.getUserId(), "id", "name");
            talkDetail.setName(sysUser.getName());
        }

        ModelAndView mav = new ModelAndView("chou/talk/talk");
        mav.addObject("talkDetail", talkDetail);

        return mav;
    }

    @PostMapping
    public R save(@RequestBody @Valid TalkDetail talkDetail) {
        talkDetail.setCreateId(SessionUtil.getUserId());
        boolean save = talkDetailService.save(talkDetail);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PutMapping
    public R edit(@RequestBody @Valid TalkDetail talkDetail) {
        if (StrUtil.isBlank(talkDetail.getId())) {
            return R.fail("非法请求参数");
        }

        int count = talkDetailService.count("id", ConditionEnum.EQ, talkDetail.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新重试");
        }

        talkDetail.setUpdateId(SessionUtil.getUserId());
        boolean update = talkDetailService.updateById(talkDetail);
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
        boolean remove = talkDetailService.removeByIds(idList);
        if (remove) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @GetMapping
    public ModelAndView detail(String talkId) {
        if (StrUtil.isBlank(talkId)) {
            throw new MyException(400, "非法请求参数");
        }

        List<TalkDetail> list = talkDetailService.list("talk_id", ConditionEnum.EQ, talkId, ConditionEnum.DESC, "rate_time", "id");
        Set<String> userIds = list.stream().map(TalkDetail::getUserId).collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(userIds)) {
            List<SysUser> users = sysUserService.list("id", ConditionEnum.IN, userIds, "id", "name");
            if (CollUtil.isNotEmpty(users)) {
                list.forEach(talkDetail -> {
                    users.forEach(u -> {
                        if (StrUtil.equals(talkDetail.getUserId(), u.getId())) {
                            talkDetail.setName(u.getName());
                        }
                    });
                });
            }
        }

        ModelAndView mav = new ModelAndView("chou/talk/detail");
        mav.addObject("list", list);

        return mav;
    }

}
