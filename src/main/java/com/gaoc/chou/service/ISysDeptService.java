package com.gaoc.chou.service;

import com.alibaba.fastjson.JSONObject;
import com.gaoc.chou.model.SysDept;
import com.gaoc.core.service.BaseService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 部门信息表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
public interface ISysDeptService extends BaseService<SysDept> {

    List<SysDept> listDeptByCompanyId(String id);

    List<JSONObject> toJsonList(List<SysDept> list);

    List<JSONObject> deptTree(List<JSONObject> list, String exId);

    List<JSONObject> oppoJsonList(List<SysDept> list);

    boolean removeDept(List<String> idList);

    Map<String, SysDept> oppoDept(Set<String> deptIds);

    Set<String> oppoIds(String id);

}
