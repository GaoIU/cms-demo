package com.gaoc.chou.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gaoc.chou.mapper.SysUserMapper;
import com.gaoc.chou.model.SysUser;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.chou.vo.UserRoleVO;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.service.BaseServiceImpl;
import com.gaoc.sys.model.SysUserRole;
import com.gaoc.sys.service.ISysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 员工信息表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final ISysUserRoleService sysUserRoleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deployRole(UserRoleVO userRoleVO) {
        sysUserRoleService.remove("sys_user_id", ConditionEnum.EQ, userRoleVO.getSysUserId());

        List<SysUserRole> list = userRoleVO.getSysRoleIds().stream().map(sysRoleId -> new SysUserRole(userRoleVO.getSysUserId(), sysRoleId))
                .collect(Collectors.toList());
        boolean saveBatch = sysUserRoleService.saveBatch(list);

        return saveBatch;
    }

    @Override
    public IPage<Map<String, Object>> allUser(Integer current, Integer size, String name, Integer type) {
        IPage<Map<String, Object>> page = new Page<>(current, size);
        return baseMapper.allUser(name, type, page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserNullByPostIds(List<String> idList) {
        baseMapper.updateUserNullByPostIds(idList);
    }

    @Override
    public void updateUserNullByDeptIds(List<String> idList) {
        baseMapper.updateUserNullByDeptIds(idList);
    }

    @Override
    public List<SysUser> selectAttenUsers() {
        return null;
    }

}
