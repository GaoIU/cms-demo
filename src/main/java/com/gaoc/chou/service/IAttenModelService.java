package com.gaoc.chou.service;

import com.gaoc.chou.model.AttenModel;
import com.gaoc.core.model.R;
import com.gaoc.core.service.BaseService;

/**
 * <p>
 * 考勤模板表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-15
 */
public interface IAttenModelService extends BaseService<AttenModel> {

    R generate(String id);
}
