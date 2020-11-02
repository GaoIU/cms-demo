package com.gaoc.chou.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaoc.chou.model.SysUser;
import com.gaoc.chou.vo.UserRoleVO;
import com.gaoc.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工信息表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
public interface ISysUserService extends BaseService<SysUser> {

    boolean deployRole(UserRoleVO userRoleVO);

    IPage<Map<String, Object>> allUser(Integer current, Integer size, String name, Integer type);

    void updateUserNullByPostIds(List<String> idList);

    void updateUserNullByDeptIds(List<String> idList);

    List<SysUser> selectAttenUsers();

}
