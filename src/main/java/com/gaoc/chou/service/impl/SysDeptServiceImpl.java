package com.gaoc.chou.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoc.chou.mapper.SysDeptMapper;
import com.gaoc.chou.model.SysDept;
import com.gaoc.chou.service.ISysDeptService;
import com.gaoc.chou.service.ISysPostService;
import com.gaoc.chou.service.ISysUserService;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门信息表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@RequiredArgsConstructor
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    private final ISysPostService sysPostService;

    private final ISysUserService sysUserService;

    @Override
    public List<SysDept> listDeptByCompanyId(String id) {
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseConstant.INT_TRUE);
        if (StrUtil.isNotBlank(id)) {
            wrapper.ne("id", id);
        }
        wrapper.and(i -> i.isNull("parent_id").or().eq("parent_id", ""));
        wrapper.orderByAsc("sort");
        return list(wrapper);
    }

    @Override
    public List<JSONObject> toJsonList(List<SysDept> list) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }

        List<JSONObject> collect = list.stream().map(sysDept -> {
            JSONObject obj = new JSONObject();
            obj.put("id", sysDept.getId());
            obj.put("name", sysDept.getName());
            obj.put("status", sysDept.getStatus());

            String parentId = sysDept.getParentId();
            obj.put("parentId", parentId);

            List<JSONObject> children = this.getDeptByPid(sysDept.getId());
            if (CollUtil.isNotEmpty(children)) {
                obj.put("children", children);
            }

            return obj;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<JSONObject> deptTree(List<JSONObject> list, String exId) {
        list.forEach(obj -> {
            String id = obj.getString("id");
            List<JSONObject> children = this.getDeptByParentId(id, exId);
            if (CollUtil.isNotEmpty(children)) {
                obj.put("children", children);
            }

            if (CollUtil.isNotEmpty(children)) {
                this.deptTree(children, exId);
            }
        });

        return list;
    }

    @Override
    public List<JSONObject> oppoJsonList(List<SysDept> records) {
        if (CollUtil.isEmpty(records)) {
            return new ArrayList<>(0);
        }

        // 获取顶级节点
        List<SysDept> topList = records.stream().filter(sysDept -> StrUtil.isBlank(sysDept.getParentId())).collect(Collectors.toList());
        // 数据中去除顶级节点
        records.removeAll(topList);
        if (CollUtil.isNotEmpty(records)) {
            // 获取其他不是顶级节点的顶级节点
            Set<String> parentIds = records.stream().map(SysDept::getParentId).collect(Collectors.toSet());
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

            for (SysDept sysDept : records) {
                String parentId = sysDept.getParentId();
                if (StrUtil.equals(id, parentId)) {
                    JSONObject tree = this.toJsonObj(sysDept);
                    children.add(tree);
                    removeIds.add(sysDept.getId());
                }
            }

            if (children.size() >= 2) {
                children.sort((c1, c2) -> c1.getInteger("sort").compareTo(c2.getInteger("sort")));
            }
            top.put("children", children);
        }
        records = records.stream().filter(sysDept -> removeIds.stream().noneMatch(id -> StrUtil.equals(id, sysDept.getId())))
                .collect(Collectors.toList());

        // 不属于顶级节点的数据
        tops = this.oppoTree(tops, records);

        return tops;
    }

    private List<SysDept> getTops(List<SysDept> topList, Set<String> parentIds) {
        List<SysDept> list = list("id", ConditionEnum.IN, parentIds);
        List<SysDept> tops = list.stream().filter(sysDept -> StrUtil.isBlank(sysDept.getParentId())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(tops)) {
            if (CollUtil.isEmpty(topList)) {
                topList.addAll(tops);
            } else {
                List<SysDept> adds = tops.stream().filter(sysDept -> topList.stream().noneMatch(sr -> StrUtil.equals(sysDept.getId(), sr.getId())))
                        .collect(Collectors.toList());
                topList.addAll(adds);
            }
        }

        parentIds = list.stream().filter(sysDept -> StrUtil.isNotBlank(sysDept.getParentId())).map(SysDept::getParentId)
                .collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(parentIds)) {
            this.getTops(topList, parentIds);
        }

        return topList;
    }

    private List<JSONObject> oppoTree(List<JSONObject> tops, List<SysDept> records) {
        for (JSONObject top : tops) {
            String id = top.getString("id");
            List<JSONObject> list = top.getJSONArray("children").toJavaList(JSONObject.class);

            for (SysDept sysDept : records) {
                this.buildTree(id, list, this.toJsonObj(sysDept));
            }

            list.sort((c1, c2) -> c1.getInteger("sort").compareTo(c2.getInteger("sort")));
            top.put("children", list);
        }

        return tops;
    }

    private void buildTree(String id, List<JSONObject> list, JSONObject sysDept) {
        String parentId = sysDept.getString("parentId");
        SysDept dept = getOne("id", ConditionEnum.EQ, parentId);
        JSONObject tree = this.toJsonObj(dept);

        List<JSONObject> children = tree.getJSONArray("children").toJavaList(JSONObject.class);
        children.add(sysDept);
        if (children.size() >= 2) {
            children.sort((c1, c2) -> c1.getInteger("sort").compareTo(c2.getInteger("sort")));
        }
        tree.put("children", children);

        if (StrUtil.equals(id, dept.getParentId())) {
            this.buildChildren(list, tree);
        } else if (StrUtil.equals(id, dept.getId())) {
            this.buildChildren(list, sysDept);
        } else if (StrUtil.isNotBlank(dept.getParentId())) {
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

    private JSONObject toJsonObj(SysDept sysDept) {
        JSONObject obj = new JSONObject(7);
        obj.put("id", sysDept.getId());
        obj.put("name", sysDept.getName());
        obj.put("status", sysDept.getStatus());
        obj.put("parentId", sysDept.getParentId());
        obj.put("sort", sysDept.getSort());
        obj.put("open", true);
        obj.put("children", new ArrayList<>(0));
        return obj;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeDept(List<String> idList) {
        boolean remove = removeByIds(idList);
        if (remove) {
            sysPostService.updatePostNullByDeptIds(idList);
            sysUserService.updateUserNullByDeptIds(idList);
        }

        return remove;
    }

    @Override
    public Map<String, SysDept> oppoDept(Set<String> deptIds) {
        Map<String, SysDept> data = new HashMap<>(deptIds.size());

        deptIds.forEach(id -> {
            SysDept sysDept = getById(id);
            if (sysDept != null) {
                if (StrUtil.isBlank(sysDept.getParentId())) {
                    data.put(id, sysDept);
                } else {
                    data.put(id, this.getTopNode(sysDept.getParentId()));
                }
            }
        });

        return data;
    }

    @Override
    public Set<String> oppoIds(String id) {
        Set<String> ids = new HashSet<>();
        ids.add(id);
        return this.getIdNode(ids, ids);
    }

    private Set<String> getIdNode(Set<String> ids, Set<String> paramIds) {
        List<SysDept> list = list("parent_id", ConditionEnum.IN, paramIds, "id");
        if (CollUtil.isEmpty(list)) {
            return ids;
        }

        Set<String> collect = list.stream().map(SysDept::getId).collect(Collectors.toSet());
        ids.addAll(collect);

        return this.getIdNode(ids, collect);
    }

    private SysDept getTopNode(String parentId) {
        SysDept sysDept = getById(parentId);
        if (StrUtil.isBlank(sysDept.getParentId())) {
            return sysDept;
        }

        return this.getTopNode(sysDept.getParentId());
    }

    private List<JSONObject> getDeptByParentId(String parentId, String exId) {
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseConstant.INT_TRUE).eq("parent_id", parentId);
        if (StrUtil.isNotBlank(exId)) {
            wrapper.ne("id", exId);
        }
        wrapper.orderByAsc("sort");
        return this.toJsonList(list(wrapper));
    }

    private List<JSONObject> getDeptByPid(String parentId) {
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseConstant.INT_TRUE).eq("parent_id", parentId);
        wrapper.orderByAsc("sort");

        List<SysDept> list = list(wrapper);
        return this.toJsonList(list);
    }

}
