package com.gaoc.chou.service;

import com.gaoc.chou.model.OrderBasic;
import com.gaoc.core.model.R;
import com.gaoc.core.service.BaseService;

/**
 * <p>
 * 客户洽谈表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-31
 */
public interface IOrderBasicService extends BaseService<OrderBasic> {

    R talk(String id, Integer status);

}
