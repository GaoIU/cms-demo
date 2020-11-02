package com.gaoc.security.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gaoc.chou.model.SysUser;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.util.SessionUtil;
import com.gaoc.sys.model.SysResource;
import com.gaoc.sys.service.ISysResourceService;
import com.gaoc.sys.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 权限验证
 * @Date: 2020/7/1 13:35
 * @Author: Gaoc
 */
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final RedisTemplate<String, Object> redisTemplate;

    private final ISysUserService sysUserService;

    private final ISysRoleService sysRoleService;

    private final ISysResourceService sysResourceService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getOne("mobile", ConditionEnum.EQ, username);
        redisTemplate.delete(BaseConstant.REDIS_MENU);
        return new User(username, sysUser.getPassword(), this.authorities(sysUser.getId()));
    }

    private Set<GrantedAuthority> authorities(String sysUserId) {
        Set<GrantedAuthority> authorities;
        List<JSONObject> list = this.getResourceList(sysUserId);

        if (CollUtil.isEmpty(list)) {
            authorities = new HashSet<>(1);
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("GUST");
            authorities.add(grantedAuthority);
            return authorities;
        }

        authorities = list.stream().map(obj -> {
            String roleCode = obj.getString("code") + "@" + obj.getString("method");
            return new SimpleGrantedAuthority(roleCode);
        }).collect(Collectors.toSet());

        return authorities;
    }

    private List<JSONObject> getResourceList(String sysUserId) {
        String key = BaseConstant.REDIS_SYS_RESOURCE + sysUserId;
        List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(obj -> JSONObject.parseObject(obj.toString())).collect(Collectors.toList());
        }

        boolean isAdmin = sysRoleService.isAdmin(sysUserId);
        List<SysResource> resources;

        if (isAdmin) {
            resources = sysResourceService.list("status", ConditionEnum.EQ, BaseConstant.INT_TRUE);
        } else {
            Set<String> sysRoleIds = sysRoleService.getSysRoleIdsBySysUserId(sysUserId);
            resources = sysResourceService.listBySysRoleIds(sysRoleIds);
        }

        if (CollUtil.isEmpty(resources)) {
            return null;
        }

        List<JSONObject> collect = resources.stream().map(sysResource -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", sysResource.getId());
            jsonObject.put("name", sysResource.getName());
            jsonObject.put("code", sysResource.getCode());
            jsonObject.put("url", sysResource.getUrl());
            jsonObject.put("method", sysResource.getMethod());
            redisTemplate.opsForList().leftPush(key, JSON.toJSONString(jsonObject));
            return jsonObject;
        }).collect(Collectors.toList());
        redisTemplate.expire(key, Duration.ofMinutes(30L));

        return collect;
    }

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return false;
        }

        Object attribute = request.getSession().getAttribute(BaseConstant.DEFAULT_SESSION_KEY);
        if (attribute == null) {
            return false;
        }

        SysUser sysUser = (SysUser) attribute;
        SessionUtil.setUserId(sysUser.getId());
        List<JSONObject> list = this.getResourceList(sysUser.getId());
        if (CollUtil.isEmpty(list)) {
            return false;
        }

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        boolean permission = list.stream().anyMatch(obj -> antPathMatcher.match(requestURI, obj.getString("url"))
                && StrUtil.equals(method, obj.getString("method")));

        return permission;
    }

}
