package com.gaoc.sys.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gaoc.chou.model.SysUser;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.R;
import com.gaoc.core.properties.BaseProperties;
import com.gaoc.sys.model.SysRoleResource;
import com.gaoc.sys.service.ISysResourceService;
import com.gaoc.sys.service.ISysRoleResourceService;
import com.gaoc.sys.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 平台中心控制器
 * @Date: 2020/7/1 11:22
 * @Author: Gaoc
 */
@RequiredArgsConstructor
@RestController
public class IndexController {

    private static final String AJAX_HEADER = "X-Requested-With";

    private final String AJAX_VALUE = "XMLHttpRequest";

    private final BaseProperties baseProperties;

    private final AuthenticationManager authenticationManager;

    private final ISysUserService sysUserService;

    private final ISysRoleService sysRoleService;

    private final ISysResourceService sysResourceService;

    private final ISysRoleResourceService sysRoleResourceService;

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/")
    public ModelAndView login() {
        return new ModelAndView("index/login");
    }

    @GetMapping("/invalid")
    public void invalid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isAjax = false;
        String header = request.getHeader(AJAX_HEADER);
        if (StrUtil.isNotBlank(header) && StrUtil.equals(header, AJAX_VALUE)) {
            isAjax = true;
        }

        if (isAjax) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            response.sendRedirect("/");
        }
    }

    @PostMapping("/signIn")
    public R signIn(@RequestBody Map<String, String> param, HttpSession session) {
        String username = param.get(baseProperties.getUsernameParameter());
        String password = param.get(baseProperties.getPasswordParameter());
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return R.fail("账号或密码不能为空");
        }

        SysUser sysUser = sysUserService.getOne("mobile", ConditionEnum.EQ, username);
        if (sysUser == null) {
            return R.fail("账号或密码错误");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, sysUser.getPassword())) {
            return R.fail("账号或密码错误");
        }
        if (sysUser.getIsDisable() == BaseConstant.INT_TRUE) {
            return R.fail("该账号已被禁用，禁止使用");
        }
        if (sysUser.getStatus() == BaseConstant.INT_FALSE) {
            return R.fail("该账户员工已离职，禁止使用");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(BaseConstant.SPRING_SECURITY_CONTEXT, SecurityContextHolder.getContext());
        session.setAttribute(BaseConstant.DEFAULT_SESSION_KEY, sysUser);

        String key = BaseConstant.REDIS_SYS_RESOURCE + sysUser.getId();
        List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
        Set<String> roleCodes = list.stream().map(obj -> {
            JSONObject jsonObj = JSONObject.parseObject(obj.toString());
            String roleCode = jsonObj.getString("code") + "@" + jsonObj.getString("method");
            return roleCode;
        }).collect(Collectors.toSet());

        return R.ok(roleCodes);
    }

    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index/index");
    }

    @PostMapping("/index")
    public R menu(HttpSession session) {
        SysUser sysUser = (SysUser) session.getAttribute(BaseConstant.DEFAULT_SESSION_KEY);
        String userId = sysUser.getId();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(BaseConstant.REDIS_MENU);

        if (entries.get(userId) != null) {
            List<JSONObject> list = JSON.parseArray(entries.get(userId).toString(), JSONObject.class);
            return R.ok(list);
        }

        Set<String> sysResourceIds = null;
        boolean isAdmin = sysRoleService.isAdmin(userId);
        if (!isAdmin) {
            Set<String> sysRoleIds = sysRoleService.getSysRoleIdsBySysUserId(userId);
            if (CollUtil.isEmpty(sysRoleIds)) {
                return R.ok(new ArrayList<>(0));
            }

            List<SysRoleResource> roleResourceList = sysRoleResourceService.list("sys_role_id", ConditionEnum.IN, sysRoleIds);
            if (CollUtil.isNotEmpty(roleResourceList)) {
                sysResourceIds = roleResourceList.stream().map(SysRoleResource::getSysResourceId).collect(Collectors.toSet());
            }
        }

        List<JSONObject> menu = sysResourceService.getMenu(sysResourceIds, true);
        List<JSONObject> treeMenu = sysResourceService.treeMenu(menu, sysResourceIds, true, true, null);
        entries.put(userId, JSON.toJSONString(treeMenu));

        redisTemplate.opsForHash().putAll(BaseConstant.REDIS_MENU, entries);
        redisTemplate.expire(BaseConstant.REDIS_MENU, Duration.ofMinutes(30L));

        return R.ok(treeMenu);
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("index/home");
    }

    @GetMapping("/info")
    public ModelAndView info(HttpSession session) {
        ModelAndView mav = new ModelAndView("index/info");

        SysUser sysUser = (SysUser) session.getAttribute(BaseConstant.DEFAULT_SESSION_KEY);
        sysUser = sysUserService.getOne("id", ConditionEnum.EQ, sysUser.getId());
        session.setAttribute(BaseConstant.DEFAULT_SESSION_KEY, sysUser);

        mav.addObject("sysUser", sysUser);
        return mav;
    }

    @GetMapping("/password")
    public ModelAndView pwdPage() {
        return new ModelAndView("index/password");
    }

    @PostMapping("/password")
    public R password(HttpSession session, String password, String newPwd, String reNewPwd) {
        if (StrUtil.isBlank(password) || StrUtil.isBlank(newPwd) || StrUtil.isBlank(reNewPwd)) {
            return R.fail("密码不能为空");
        }

        if (!newPwd.equals(reNewPwd)) {
            return R.fail("两次密码不一致");
        }

        SysUser sysUser = (SysUser) session.getAttribute(BaseConstant.DEFAULT_SESSION_KEY);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, sysUser.getPassword())) {
            return R.fail("原密码错误");
        }

        sysUser.setPassword(passwordEncoder.encode(newPwd));
        sysUser.setUpdateId(sysUser.getId());
        boolean update = sysUserService.updateById(sysUser);
        if (update) {
            session.setAttribute(BaseConstant.DEFAULT_SESSION_KEY, sysUser);
            return R.ok();
        }

        return R.fail("操作失败");
    }

    @GetMapping("/403")
    public ModelAndView error403() {
        return new ModelAndView("error/403");
    }

    @GetMapping("/404")
    public ModelAndView error404() {
        return new ModelAndView("error/404");
    }

    @GetMapping("/500")
    public ModelAndView error500() {
        return new ModelAndView("error/500");
    }

}
