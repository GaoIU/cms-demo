package com.gaoc.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoc.chou.util.EnumUtil;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.service.BaseServiceImpl;
import com.gaoc.sys.mapper.SysResourceMapper;
import com.gaoc.sys.model.SysResource;
import com.gaoc.sys.model.SysRoleResource;
import com.gaoc.sys.service.ISysResourceService;
import com.gaoc.sys.service.ISysRoleResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台资源表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@RequiredArgsConstructor
@Service
public class SysResourceServiceImpl extends BaseServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {

    private final ISysRoleResourceService sysRoleResourceService;

    @Override
    public List<SysResource> listBySysRoleIds(Set<String> sysRoleIds) {
        if (CollUtil.isEmpty(sysRoleIds)) {
            return null;
        }

        List<SysRoleResource> roleResourceList = sysRoleResourceService.list("sys_role_id", ConditionEnum.IN, sysRoleIds);
        if (CollUtil.isEmpty(roleResourceList)) {
            return null;
        }

        Set<String> sysResourceIds = roleResourceList.stream().map(SysRoleResource::getSysResourceId).collect(Collectors.toSet());
        QueryWrapper<SysResource> condition = new QueryWrapper<>();
        condition.eq("status", BaseConstant.INT_TRUE).in("id", sysResourceIds);
        return list(condition);
    }

    @Override
    public List<JSONObject> getMenu(Set<String> sysResourceIds, boolean isMenu) {
        QueryWrapper<SysResource> condition = new QueryWrapper<>();
        condition.eq("status", BaseConstant.INT_TRUE);
        condition.and(i -> i.isNull("parent_id").or().eq("parent_id", ""));
        condition.orderByAsc("sort");
        if (CollUtil.isNotEmpty(sysResourceIds)) {
            condition.in("id", sysResourceIds);
        }

        List<SysResource> resourceList = list(condition);
        if (CollUtil.isEmpty(resourceList)) {
            return new ArrayList<>(0);
        }

        return this.toJsonList(resourceList);
    }

    @Override
    public List<JSONObject> toJsonList(List<SysResource> list) {
        return list.stream().map(sysResource -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", sysResource.getId());
            jsonObject.put("name", sysResource.getName());
            jsonObject.put("title", sysResource.getName());
            jsonObject.put("code", sysResource.getCode());
            jsonObject.put("url", sysResource.getUrl());
            jsonObject.put("type", sysResource.getType());
            jsonObject.put("status", sysResource.getStatus());
            jsonObject.put("parentId", sysResource.getParentId());
            return jsonObject;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeResourceByIds(List<String> ids) {
        boolean remove = removeByIds(ids);
        if (remove) {
            sysRoleResourceService.remove("sys_resource_id", ConditionEnum.IN, ids);
        }
        return remove;
    }

    @Override
    public List<JSONObject> roleTree(List<JSONObject> list) {
        list.forEach(obj -> {
            String id = obj.getString("id");
            List<JSONObject> children = this.getResourceListByParentId(id);
            obj.put("children", children);

            if (CollUtil.isNotEmpty(children)) {
                this.roleTree(children);
            }
        });

        return list;
    }

    @Override
    public List<JSONObject> oppoJsonList(List<SysResource> records) {
        if (CollUtil.isEmpty(records)) {
            return new ArrayList<>(0);
        }

        // 获取顶级节点
        List<SysResource> topList = records.stream().filter(sysResource -> StrUtil.isBlank(sysResource.getParentId())).collect(Collectors.toList());
        // 数据中去除顶级节点
        records.removeAll(topList);
        if (CollUtil.isNotEmpty(records)) {
            // 获取其他不是顶级节点的顶级节点
            Set<String> parentIds = records.stream().map(SysResource::getParentId).collect(Collectors.toSet());
            topList = this.getTops(topList, parentIds);
        }
        // 将顶级节点转换为json形式数据
        List<JSONObject> tops = topList.stream().map(sysResource -> this.toJsonObj(sysResource))
                .sorted((c1, c2) -> c1.getInteger("sort").compareTo(c2.getInteger("sort"))).collect(Collectors.toList());

        // 处理掉直接属于上级节点的数据
        Set<String> removeIds = new HashSet<>(records.size());
        for (JSONObject top : tops) {
            String id = top.getString("id");
            List<JSONObject> children = top.getJSONArray("children").toJavaList(JSONObject.class);

            for (SysResource sysResource : records) {
                String parentId = sysResource.getParentId();
                if (StrUtil.equals(id, parentId)) {
                    JSONObject tree = this.toJsonObj(sysResource);
                    children.add(tree);
                    removeIds.add(sysResource.getId());
                }
            }

            if (children.size() >= 2) {
                children.sort((c1, c2) -> c1.getInteger("sort").compareTo(c2.getInteger("sort")));
            }
            top.put("children", children);
        }
        records = records.stream().filter(sysResource -> removeIds.stream().noneMatch(id -> StrUtil.equals(id, sysResource.getId())))
                .collect(Collectors.toList());

        // 不属于顶级节点的数据
        tops = this.oppoTree(tops, records);

        return tops;
    }

    private List<SysResource> getTops(List<SysResource> topList, Set<String> parentIds) {
        List<SysResource> list = list("id", ConditionEnum.IN, parentIds);
        List<SysResource> tops = list.stream().filter(sysResource -> StrUtil.isBlank(sysResource.getParentId())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(tops)) {
            if (CollUtil.isEmpty(topList)) {
                topList.addAll(tops);
            } else {
                List<SysResource> adds = tops.stream().filter(sysResource -> topList.stream().noneMatch(sr -> StrUtil.equals(sysResource.getId(), sr.getId())))
                        .collect(Collectors.toList());
                topList.addAll(adds);
            }
        }

        parentIds = list.stream().filter(sysResource -> StrUtil.isNotBlank(sysResource.getParentId())).map(SysResource::getParentId)
                .collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(parentIds)) {
            this.getTops(topList, parentIds);
        }

        return topList;
    }

    private List<JSONObject> oppoTree(List<JSONObject> tops, List<SysResource> records) {
        for (JSONObject top : tops) {
            String id = top.getString("id");
            List<JSONObject> list = top.getJSONArray("children").toJavaList(JSONObject.class);

            for (SysResource sysResource : records) {
                this.buildTree(id, list, this.toJsonObj(sysResource));
            }

            list.sort((c1, c2) -> c1.getInteger("sort").compareTo(c2.getInteger("sort")));
            top.put("children", list);
        }

        return tops;
    }

    private void buildTree(String id, List<JSONObject> list, JSONObject sysResource) {
        String parentId = sysResource.getString("parentId");
        SysResource resource = getOne("id", ConditionEnum.EQ, parentId);
        JSONObject tree = this.toJsonObj(resource);

        List<JSONObject> children = tree.getJSONArray("children").toJavaList(JSONObject.class);
        children.add(sysResource);
        if (children.size() >= 2) {
            children.sort((c1, c2) -> c1.getInteger("sort").compareTo(c2.getInteger("sort")));
        }
        tree.put("children", children);

        if (StrUtil.equals(id, resource.getParentId())) {
            this.buildChildren(list, tree);
        } else if (StrUtil.equals(id, resource.getId())) {
            this.buildChildren(list, sysResource);
        } else if (StrUtil.isNotBlank(resource.getParentId())) {
            this.buildTree(id, list, tree);
        }
    }

    private void buildChildren(List<JSONObject> list, JSONObject tree) {
        String id = tree.getString("id");
        boolean anyMatch = list.stream().anyMatch(obj -> StrUtil.equals(obj.getString("id"), id));
        if (anyMatch) {
            list.forEach(obj -> {
                String objId = obj.getString("id");
                List<JSONObject> children = tree.getJSONArray("children").toJavaList(JSONObject.class);
                if (StrUtil.equals(id, objId) && CollUtil.isNotEmpty(children)) {
                    List<JSONObject> objChildren = obj.getJSONArray("children").toJavaList(JSONObject.class);
                    if (CollUtil.isEmpty(objChildren)) {
                        objChildren.addAll(children);
                    } else {
                        JSONObject object = children.get(0);
                        this.buildChildren(objChildren, object);
                    }

                    if (objChildren.size() >= 2) {
                        objChildren.sort((c1, c2) -> c1.getInteger("sort").compareTo(c2.getInteger("sort")));
                    }
                    obj.put("children", objChildren);
                }
            });
        } else {
            list.add(tree);
        }
    }

    private JSONObject toJsonObj(SysResource sysResource) {
        JSONObject obj = new JSONObject(10);
        obj.put("id", sysResource.getId());
        obj.put("name", sysResource.getName());
        obj.put("code", sysResource.getCode());
        obj.put("url", sysResource.getUrl());
        obj.put("method", sysResource.getMethod());
        obj.put("type", sysResource.getType());
        obj.put("sort", sysResource.getSort());
        obj.put("open", true);
        obj.put("parentId", sysResource.getParentId());
        obj.put("status", sysResource.getStatus());
        obj.put("children", new ArrayList<>(0));
        return obj;
    }

    @Override
    public List<JSONObject> treeMenu(List<JSONObject> list, Set<String> sysResourceIds, boolean isMenu, boolean isTrue, String exId) {
        list.forEach(jsonObject -> {
            String id = jsonObject.getString("id");
            List<JSONObject> children = this.getResourceListByParentId(id, sysResourceIds, isMenu, isTrue, exId);
            jsonObject.put("children", children);

            String name = jsonObject.getString("name");
            Integer type = jsonObject.getInteger("type");
            String title = name + "（" + EnumUtil.sysResourceType(type) + "）";
            jsonObject.put("title", title);

            if (CollUtil.isNotEmpty(children)) {
                this.treeMenu(children, sysResourceIds, isMenu, isTrue, exId);
            }
        });

        return list;
    }

    private List<JSONObject> getResourceListByParentId(String parentId, Set<String> sysResourceIds, boolean isMenu, boolean isTrue, String exId) {
        QueryWrapper<SysResource> condition = new QueryWrapper<>();
        condition.eq("parent_id", parentId);
        if (CollUtil.isNotEmpty(sysResourceIds)) {
            condition.in("id", sysResourceIds);
        }
        if (StrUtil.isNotBlank(exId)) {
            condition.ne("id", exId);
        }
        condition.orderByAsc("sort");
        if (isMenu) {
            condition.eq("type", BaseConstant.RESOURCE_TYPE_MENU);
        }
        if (isTrue) {
            condition.eq("status", BaseConstant.INT_TRUE);
        }

        List<SysResource> list = list(condition);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>(0);
        }

        return this.toJsonList(list);
    }

    private List<JSONObject> getResourceListByParentId(String parentId) {
        QueryWrapper<SysResource> condition = new QueryWrapper<>();
        condition.eq("parent_id", parentId).eq("status", BaseConstant.INT_TRUE);
        condition.ne("type", BaseConstant.RESOURCE_TYPE_FUN).orderByAsc("sort");
        List<SysResource> list = list(condition);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>(0);
        }

        return this.toJsonList(list);
    }

}
