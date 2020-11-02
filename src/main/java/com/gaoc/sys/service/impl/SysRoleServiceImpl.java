package com.gaoc.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.service.BaseServiceImpl;
import com.gaoc.sys.mapper.SysRoleMapper;
import com.gaoc.sys.model.SysRole;
import com.gaoc.sys.model.SysRoleResource;
import com.gaoc.sys.model.SysUserRole;
import com.gaoc.sys.service.ISysRoleResourceService;
import com.gaoc.sys.service.ISysRoleService;
import com.gaoc.sys.service.ISysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台角色表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final ISysRoleResourceService sysRoleResourceService;

    private final ISysUserRoleService sysUserRoleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSysRole(SysRole sysRole, List<SysRoleResource> list) {
        boolean save = save(sysRole);
        if (save && CollUtil.isNotEmpty(list)) {
            list.forEach(srr -> srr.setSysRoleId(sysRole.getId()));
            sysRoleResourceService.saveBatch(list);
        }

        return save;
    }

    @Override
    public boolean isAdmin(String sysUserId) {
        List<SysUserRole> userRoleList = sysUserRoleService.list("sys_user_id", ConditionEnum.EQ, sysUserId);
        if (CollUtil.isEmpty(userRoleList)) {
            return false;
        }

        Set<String> sysRoleIds = userRoleList.stream().map(SysUserRole::getSysRoleId).collect(Collectors.toSet());
        QueryWrapper<SysRole> condition = new QueryWrapper<>();
        condition.eq("code", BaseConstant.DEFAULT_ROLE_CODE);
        condition.in("id", sysRoleIds).eq("status", BaseConstant.INT_TRUE);
        int count = count(condition);

        return count > 0;
    }

    @Override
    public Set<String> getSysRoleIdsBySysUserId(String sysUserId) {
        List<SysUserRole> userRoleList = sysUserRoleService.list("sys_user_id", ConditionEnum.EQ, sysUserId);
        if (CollUtil.isEmpty(userRoleList)) {
            return null;
        }

        Set<String> sysRoleIds = userRoleList.stream().map(SysUserRole::getSysRoleId).collect(Collectors.toSet());
        QueryWrapper<SysRole> condition = new QueryWrapper<>();
        condition.in("id", sysRoleIds).eq("status", BaseConstant.INT_TRUE);
        List<SysRole> roleList = list(condition);
        if (CollUtil.isEmpty(roleList)) {
            return null;
        }

        return roleList.stream().map(SysRole::getId).collect(Collectors.toSet());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean editSysRole(SysRole sysRole, List<SysRoleResource> list) {
        boolean update = updateById(sysRole);
        List<String> sysResourceIds = sysRole.getSysResourceIds();
        if (update && CollUtil.isNotEmpty(sysResourceIds)) {
            sysRoleResourceService.remove("sys_role_id", ConditionEnum.EQ, sysRole.getId());
            sysRoleResourceService.saveBatch(list);
        }

        return update;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeRoleByIds(List<String> idList) {
        boolean remove = removeByIds(idList);
        if (remove) {
            sysRoleResourceService.remove("sys_role_id", ConditionEnum.IN, idList);
        }

        return remove;
    }

}
