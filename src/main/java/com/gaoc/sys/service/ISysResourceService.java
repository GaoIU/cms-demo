package com.gaoc.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.gaoc.core.service.BaseService;
import com.gaoc.sys.model.SysResource;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 后台资源表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
public interface ISysResourceService extends BaseService<SysResource> {

    List<SysResource> listBySysRoleIds(Set<String> sysRoleIds);

    List<JSONObject> getMenu(Set<String> sysResourceIds, boolean isMenu);

    List<JSONObject> treeMenu(List<JSONObject> list, Set<String> sysResourceIds, boolean isMenu, boolean isTrue, String exId);

    List<JSONObject> toJsonList(List<SysResource> list);

    boolean removeResourceByIds(List<String> ids);

    List<JSONObject> roleTree(List<JSONObject> list);

    List<JSONObject> oppoJsonList(List<SysResource> records);

}
