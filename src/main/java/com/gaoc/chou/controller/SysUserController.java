package com.gaoc.chou.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.model.SysDept;
import com.gaoc.chou.model.SysPost;
import com.gaoc.chou.model.SysUser;
import com.gaoc.chou.service.ISysDeptService;
import com.gaoc.chou.service.ISysPostService;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.chou.util.EnumUtil;
import com.gaoc.chou.vo.UserRoleVO;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.R;
import com.gaoc.core.util.SessionUtil;
import com.gaoc.sys.model.SysRole;
import com.gaoc.sys.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 员工控制器
 * @Date: 2020/7/1 17:36
 * @Author: Gaoc
 */
@RequestMapping("/sysUser")
@RequiredArgsConstructor
@RestController
public class SysUserController {

    private final ISysUserService sysUserService;

    private final ISysRoleService sysRoleService;

    private final ISysDeptService sysDeptService;

    private final ISysPostService sysPostService;

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public ModelAndView userPage() {
        return new ModelAndView("chou/user");
    }

    @GetMapping("/query")
    public R query(@RequestParam Map<String, Object> param) {
        Integer current = MapUtil.getInt(param, "current");
        Integer size = MapUtil.getInt(param, "size");
        current = current == null || current <= 0 ? 1 : current;
        size = size == null ? 20 : size;

        QueryWrapper<SysUser> condition = new QueryWrapper<>();
        IPage<SysUser> page = new Page<>(current, size);

        String name = MapUtil.getStr(param, "name");
        if (StrUtil.isNotBlank(name)) {
            condition.and(i -> i.like("name", name).or().like("nick_name", name));
        }

        String mobile = MapUtil.getStr(param, "mobile");
        if (StrUtil.isNotBlank(mobile)) {
            condition.like("mobile", mobile);
        }

        Integer status = MapUtil.getInt(param, "status");
        if (status != null) {
            condition.eq("status", status);
        }

        String deptId = MapUtil.getStr(param, "deptId");
        if (StrUtil.isNotBlank(deptId)) {
            Set<String> ids = sysDeptService.oppoIds(deptId);
            condition.and(i -> i.in("dept_id", ids).or().in("entry_dept_id", ids));
        }

        condition.orderByAsc("job_no");
        page = sysUserService.page(page, condition);
        List<SysUser> records = page.getRecords();

        Set<String> entryDeptIds = records.stream().map(SysUser::getEntryDeptId).collect(Collectors.toSet());
        Set<String> deptIds = records.stream().map(SysUser::getDeptId).collect(Collectors.toSet());
        deptIds.addAll(entryDeptIds);
        Map<String, SysDept> oppoDept = sysDeptService.oppoDept(deptIds);

        Set<String> entryPostIds = records.stream().map(SysUser::getEntryPostId).collect(Collectors.toSet());
        Set<String> postIds = records.stream().map(SysUser::getPostId).collect(Collectors.toSet());
        postIds.addAll(entryPostIds);
        List<SysPost> postList = new ArrayList<>(postIds.size());
        if (CollUtil.isNotEmpty(postIds)) {
            List<SysPost> sysPosts = sysPostService.list("id", ConditionEnum.IN, postIds, "id", "name");
            postList.addAll(sysPosts);
        }

        records.forEach(sysUser -> {
            if (MapUtil.isNotEmpty(oppoDept)) {
                SysDept entryDept = oppoDept.get(sysUser.getEntryDeptId());
                if (entryDept != null) {
                    sysUser.setEntryDeptName(entryDept.getName());
                }

                SysDept sysDept = oppoDept.get(sysUser.getDeptId());
                if (sysDept != null) {
                    sysUser.setDeptName(sysDept.getName());
                }
            }

            if (CollUtil.isNotEmpty(postList)) {
                postList.forEach(sysPost -> {
                    if (StrUtil.equals(sysUser.getEntryPostId(), sysPost.getId())) {
                        sysUser.setEntryPostName(sysPost.getName());
                    }

                    if (StrUtil.equals(sysUser.getPostId(), sysPost.getId())) {
                        sysUser.setPostName(sysPost.getName());
                    }
                });
            }

            sysUser.setSexDesc(EnumUtil.userSex(sysUser.getSex()));
            sysUser.setStatusDesc(EnumUtil.userStatus(sysUser.getStatus()));
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

    @PostMapping("/post")
    public R listPost(String deptId) {
        if (StrUtil.isBlank(deptId)) {
            return R.fail("参数请求不合法");
        }

        QueryWrapper<SysPost> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseConstant.INT_TRUE).eq("dept_id", deptId);
        wrapper.orderByAsc("sort").orderByDesc("update_time");
        return R.ok(sysPostService.list(wrapper));
    }

    @PostMapping
    public R save(@RequestBody @Valid SysUser sysUser) {
        R r = this.checkParam(sysUser);
        if (R.isFail(r)) {
            return r;
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(BaseConstant.DEFAULT_PASSWORD);
        sysUser.setPassword(password);
        sysUser.setCreateId(SessionUtil.getUserId());
        sysUser.setIsDisable(BaseConstant.INT_TRUE);

        boolean save = sysUserService.save(sysUser);
        if (save) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    private R checkParam(SysUser sysUser) {
        R checkJobNo = this.checkJobNo(sysUser.getId(), sysUser.getJobNo());
        if (R.isFail(checkJobNo)) {
            return checkJobNo;
        }

        R checkMobile = this.checkMobile(sysUser.getId(), sysUser.getMobile());
        if (R.isFail(checkMobile)) {
            return checkMobile;
        }

        R checkEmail = this.checkEmail(sysUser.getId(), sysUser.getEmail());
        if (R.isFail(checkEmail)) {
            return checkEmail;
        }

        R checkCardNo = this.checkCardNo(sysUser.getId(), sysUser.getCardNo());
        if (R.isFail(checkCardNo)) {
            return checkCardNo;
        }

        R checkBankNo = this.checkBankNo(sysUser.getId(), sysUser.getBankNo());
        if (R.isFail(checkBankNo)) {
            return checkBankNo;
        }

        if (BaseConstant.INT_FALSE == sysUser.getStatus()) {
            LocalDate quitTime = sysUser.getQuitTime();
            if (quitTime == null) {
                return R.fail("请选择离职日期");
            }

            String socialEndTime = sysUser.getSocialEndTime();
            if (StrUtil.isBlank(socialEndTime)) {
                return R.fail("请选择社保缴纳最后月份");
            }
        }

        return R.ok();
    }

    @PutMapping
    public R edit(@RequestBody @Valid SysUser sysUser) {
        if (StrUtil.isBlank(sysUser.getId())) {
            return R.fail("请求参数不合法");
        }

        int count = sysUserService.count("id", ConditionEnum.EQ, sysUser.getId());
        if (count <= 0) {
            return R.fail("操作数据不存在，请刷新重试");
        }

        R r = this.checkParam(sysUser);
        if (R.isFail(r)) {
            return r;
        }

        sysUser.setUpdateId(SessionUtil.getUserId());
        boolean update = sysUserService.updateById(sysUser);
        if (update) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PostMapping("/check/jobNo")
    public R checkJobNo(String id, String jobNo) {
        boolean only = sysUserService.isOnly(id, "job_no", jobNo);
        if (!only) {
            return R.fail("该工号已被使用");
        }

        return R.ok();
    }

    @PostMapping("/check/mobile")
    public R checkMobile(String id, String mobile) {
        boolean only = sysUserService.isOnly(id, "mobile", mobile);
        if (!only) {
            return R.fail("该手机号已被使用");
        }

        return R.ok();
    }

    @PostMapping("/check/email")
    public R checkEmail(String id, String email) {
        boolean only = sysUserService.isOnly(id, "email", email);
        if (!only) {
            return R.fail("该邮箱已被使用");
        }

        return R.ok();
    }

    @PostMapping("/check/cardNo")
    public R checkCardNo(String id, String cardNo) {
        boolean only = sysUserService.isOnly(id, "card_no", cardNo);
        if (!only) {
            return R.fail("该身份证号已存在");
        }

        return R.ok();
    }

    @PostMapping("/check/bankNo")
    public R checkBankNo(String id, String bankNo) {
        boolean only = sysUserService.isOnly(id, "bank_no", bankNo);
        if (!only) {
            return R.fail("该银行卡号已存在");
        }

        return R.ok();
    }

    @GetMapping("/role")
    public R auth(String id) {
        Set<String> roleIds = sysRoleService.getSysRoleIdsBySysUserId(id);

        List<SysRole> list = sysRoleService.list("status", ConditionEnum.EQ, BaseConstant.INT_TRUE, "id", "name");
        List<JSONObject> roles = list.stream().map(sysRole -> {
            JSONObject obj = new JSONObject(3);
            obj.put("id", sysRole.getId());
            obj.put("name", sysRole.getName());
            return obj;
        }).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>(2);
        data.put("roleIds", roleIds);
        data.put("roles", roles);

        return R.ok(data);
    }

    @PostMapping("/role")
    public R deployRole(@RequestBody @Valid UserRoleVO userRoleVO) {
        boolean deployRole = sysUserService.deployRole(userRoleVO);
        if (deployRole) {
            redisTemplate.delete(BaseConstant.REDIS_MENU);
            String key = BaseConstant.REDIS_SYS_RESOURCE + SessionUtil.getUserId();
            redisTemplate.delete(key);

            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PostMapping("/password")
    public R resetPwd(String id) {
        if (StrUtil.isBlank(id)) {
            return R.fail("非法请求参数");
        }

        int count = sysUserService.count("id", ConditionEnum.EQ, id);
        if (count <= 0) {
            return R.fail("该用户不存在");
        }

        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        sysUser.setPassword(passwordEncoder.encode(BaseConstant.DEFAULT_PASSWORD));
        sysUser.setUpdateId(SessionUtil.getUserId());

        boolean update = sysUserService.updateById(sysUser);
        if (update) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @PostMapping("/enable")
    public R enable(String id, Integer enable) {
        if (StrUtil.isBlank(id) || enable == null) {
            return R.fail("非法请求参数");
        }

        if (BaseConstant.INT_FALSE == enable && BaseConstant.INT_TRUE == enable) {
            return R.fail("非法请求参数");
        }

        int count = sysUserService.count("id", ConditionEnum.EQ, id);
        if (count <= 0) {
            return R.fail("该用户不存在");
        }

        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setIsDisable(enable);
        sysUser.setUpdateId(SessionUtil.getUserId());

        boolean update = sysUserService.updateById(sysUser);
        if (update) {
            return R.ok();
        }

        return R.fail("操作失败");
    }

}
