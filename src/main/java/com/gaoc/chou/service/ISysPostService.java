package com.gaoc.chou.service;

import com.gaoc.chou.model.SysPost;
import com.gaoc.core.service.BaseService;

import java.util.List;

/**
 * <p>
 * 岗位信息表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
public interface ISysPostService extends BaseService<SysPost> {

    void updatePostNullByDeptIds(List<String> idList);

    boolean removePost(List<String> idList);

}
