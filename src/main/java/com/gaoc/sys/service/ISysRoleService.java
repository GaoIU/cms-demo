package com.gaoc.sys.service;

import com.gaoc.core.service.BaseService;
import com.gaoc.sys.model.SysRole;
import com.gaoc.sys.model.SysRoleResource;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 后台角色表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
public interface ISysRoleService extends BaseService<SysRole> {

    boolean saveSysRole(SysRole sysRole, List<SysRoleResource> list);

    boolean isAdmin(String sysUserId);

    Set<String> getSysRoleIdsBySysUserId(String sysUserId);

    boolean editSysRole(SysRole sysRole, List<SysRoleResource> list);

    boolean removeRoleByIds(List<String> idList);
}
