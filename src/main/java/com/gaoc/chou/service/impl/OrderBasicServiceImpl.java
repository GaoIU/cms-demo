package com.gaoc.chou.service.impl;

import com.gaoc.chou.mapper.OrderBasicMapper;
import com.gaoc.chou.model.OrderBasic;
import com.gaoc.chou.service.IOrderBasicService;
import com.gaoc.core.model.R;
import com.gaoc.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 客户洽谈表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-31
 */
@Service
public class OrderBasicServiceImpl extends BaseServiceImpl<OrderBasicMapper, OrderBasic> implements IOrderBasicService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R talk(String id, Integer status) {
        baseMapper.talk(id, status);
        return R.ok();
    }
}
