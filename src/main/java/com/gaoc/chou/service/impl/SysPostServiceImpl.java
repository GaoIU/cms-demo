package com.gaoc.chou.service.impl;

import com.gaoc.chou.mapper.SysPostMapper;
import com.gaoc.chou.model.SysPost;
import com.gaoc.chou.service.ISysPostService;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.core.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 岗位信息表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@RequiredArgsConstructor
@Service
public class SysPostServiceImpl extends BaseServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    private final ISysUserService sysUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePostNullByDeptIds(List<String> idList) {
        baseMapper.updatePostNullByDeptIds(idList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removePost(List<String> idList) {
        boolean remove = removeByIds(idList);
        if (remove) {
            sysUserService.updateUserNullByPostIds(idList);
        }

        return remove;
    }
}
