package com.gaoc.chou.service.impl;

import com.gaoc.chou.model.AttenModel;
import com.gaoc.chou.mapper.AttenModelMapper;
import com.gaoc.chou.model.SysUser;
import com.gaoc.chou.service.IAttenModelService;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.model.R;
import com.gaoc.core.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 考勤模板表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-15
 */
@RequiredArgsConstructor
@Service
public class AttenModelServiceImpl extends BaseServiceImpl<AttenModelMapper, AttenModel> implements IAttenModelService {

    private final ISysUserService sysUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R generate(String id) {
        AttenModel attenModel = getById(id);
        if (attenModel == null) {
            return R.fail("数据不存在，请刷新页面重试");
        }
        if (attenModel.getStatus() != BaseConstant.INT_FALSE) {
            return R.fail("不满足生成考勤数据条件");
        }

        List<SysUser> users = sysUserService.list();

        return null;
    }

}
