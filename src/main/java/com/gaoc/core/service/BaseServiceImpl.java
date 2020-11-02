package com.gaoc.core.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.BaseQuery;
import com.gaoc.core.model.OrderByModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    private static final String ARG1 = "arg1";
    private static final String ARG2 = "arg2";
    private static final String EMPTY = "";
    private static final String ID = "id";
    private static final char UNDERLINE = '_';

    @Override
    public boolean isExist(Wrapper<T> queryWrapper) {
        int count = count(queryWrapper);
        return count > 0;
    }

    @Override
    public boolean isExist(String column, ConditionEnum conditionEnum, Object val) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        int count = count(queryWrapper);
        return count > 0;
    }

    @Override
    public T getOne(String column, ConditionEnum conditionEnum, Object val) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        return getOne(queryWrapper);
    }

    @Override
    public T getOne(String column, ConditionEnum conditionEnum, Object val, String... columns) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        queryWrapper.select(columns);
        return getOne(queryWrapper);
    }

    @Override
    public List<T> list(ConditionEnum conditionEnum, String... orderBys) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (ConditionEnum.ASC == conditionEnum) {
            queryWrapper.orderByAsc(orderBys);
        }
        if (ConditionEnum.DESC == conditionEnum) {
            queryWrapper.orderByDesc(orderBys);
        }

        return list(queryWrapper);
    }

    @Override
    public List<T> list(List<OrderByModel> orderByModels) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(orderByModels)) {
            for (OrderByModel orderByModel : orderByModels) {
                ConditionEnum conditionEnum = orderByModel.getConditionEnum();
                String[] columns = orderByModel.getColumns();

                if (ConditionEnum.ASC == conditionEnum) {
                    queryWrapper.orderByAsc(columns);
                }
                if (ConditionEnum.DESC == conditionEnum) {
                    queryWrapper.orderByDesc(columns);
                }
            }
        }

        return list(queryWrapper);
    }

    @Override
    public List<T> list(String column, ConditionEnum conditionEnum, Object val) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        return list(queryWrapper);
    }

    @Override
    public List<T> list(String column, ConditionEnum conditionEnum, Object val, String... columns) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        queryWrapper.select(columns);
        return list(queryWrapper);
    }

    @Override
    public List<T> list(String column, ConditionEnum conditionEnum, Object val, OrderByModel orderByModel,
                        String... columns) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        queryWrapper.select(columns);
        if (ConditionEnum.ASC == orderByModel.getConditionEnum()) {
            queryWrapper.orderByAsc(orderByModel.getColumns());
        }
        if (ConditionEnum.DESC == orderByModel.getConditionEnum()) {
            queryWrapper.orderByDesc(orderByModel.getColumns());
        }

        return list(queryWrapper);
    }

    @Override
    public List<T> list(String column, ConditionEnum conditionEnum, Object val, List<OrderByModel> orderByModels,
                        String... columns) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        queryWrapper.select(columns);

        if (CollUtil.isNotEmpty(orderByModels)) {
            for (OrderByModel orderByModel : orderByModels) {
                if (ConditionEnum.ASC == orderByModel.getConditionEnum()) {
                    queryWrapper.orderByAsc(orderByModel.getColumns());
                }
                if (ConditionEnum.DESC == orderByModel.getConditionEnum()) {
                    queryWrapper.orderByDesc(orderByModel.getColumns());
                }
            }
        }

        return list(queryWrapper);
    }

    @Override
    public List<T> list(String column, ConditionEnum conditionEnum, Object val, ConditionEnum orderBy,
                        String... orderBys) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        if (ConditionEnum.ASC == orderBy) {
            queryWrapper.orderByAsc(orderBys);
        }
        if (ConditionEnum.DESC == orderBy) {
            queryWrapper.orderByDesc(orderBys);
        }

        return list(queryWrapper);
    }

    @Override
    public int count(String column, ConditionEnum conditionEnum, Object val) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        return count(queryWrapper);
    }

    @Override
    public boolean isOnly(Serializable id, String column, Object val) {
        return isOnly(id, ID, column, val);
    }

    @Override
    public boolean isOnly(Serializable id, String idFiled, String column, Object val) {
        boolean exist = isExist(column, ConditionEnum.EQ, val);

        if (id != null) {
            T one = getOne(idFiled, ConditionEnum.EQ, id, column);
            if (one != null) {
                Object value = ReflectionKit.getMethodValue(one, underlineToCamel(column));
                if (ObjectUtil.equal(val, value)) {
                    exist = false;
                }
            }
        }

        return !exist;
    }

    @Override
    public boolean remove(String column, ConditionEnum conditionEnum, Object val) {
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        return remove(queryWrapper);
    }

    @Override
    public IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val) {
        IPage<T> page = new Page<>(current, size);
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        return page(page, queryWrapper);
    }

    @Override
    public IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                         String... columns) {
        IPage<T> page = new Page<>(current, size);
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        queryWrapper.select(columns);
        return page(page, queryWrapper);
    }

    @Override
    public IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                         ConditionEnum orderBy, String... orderBys) {
        IPage<T> page = new Page<>(current, size);
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);

        if (ConditionEnum.ASC == orderBy) {
            queryWrapper.orderByAsc(orderBys);
        }
        if (ConditionEnum.DESC == orderBy) {
            queryWrapper.orderByDesc(orderBys);
        }

        return page(page, queryWrapper);
    }

    @Override
    public IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                         OrderByModel orderByModel, String... columns) {
        IPage<T> page = new Page<>(current, size);
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        queryWrapper.select(columns);

        if (ConditionEnum.ASC == orderByModel.getConditionEnum()) {
            queryWrapper.orderByAsc(orderByModel.getColumns());
        }
        if (ConditionEnum.DESC == orderByModel.getConditionEnum()) {
            queryWrapper.orderByDesc(orderByModel.getColumns());
        }

        return page(page, queryWrapper);
    }

    @Override
    public IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                         List<OrderByModel> orderByModels, String... columns) {
        IPage<T> page = new Page<>(current, size);
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);
        queryWrapper.select(columns);

        if (CollUtil.isNotEmpty(orderByModels)) {
            for (OrderByModel orderByModel : orderByModels) {
                if (ConditionEnum.ASC == orderByModel.getConditionEnum()) {
                    queryWrapper.orderByAsc(orderByModel.getColumns());
                }
                if (ConditionEnum.DESC == orderByModel.getConditionEnum()) {
                    queryWrapper.orderByDesc(orderByModel.getColumns());
                }
            }
        }

        return page(page, queryWrapper);
    }

    @Override
    public IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                         List<OrderByModel> orderByModels) {
        IPage<T> page = new Page<>(current, size);
        QueryWrapper<T> queryWrapper = addCondition(column, conditionEnum, val);

        if (CollUtil.isNotEmpty(orderByModels)) {
            for (OrderByModel orderByModel : orderByModels) {
                if (ConditionEnum.ASC == orderByModel.getConditionEnum()) {
                    queryWrapper.orderByAsc(orderByModel.getColumns());
                }
                if (ConditionEnum.DESC == orderByModel.getConditionEnum()) {
                    queryWrapper.orderByDesc(orderByModel.getColumns());
                }
            }
        }

        return page(page, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps(String column, ConditionEnum conditionEnum, Object val) {
        QueryWrapper<T> wrapper = addCondition(column, conditionEnum, val);
        return this.listMaps(wrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps(String column, ConditionEnum conditionEnum, Object val, String... columns) {
        QueryWrapper<T> wrapper = addCondition(column, conditionEnum, val);
        wrapper.select(columns);
        return this.listMaps(wrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps(String column, ConditionEnum conditionEnum, Object val, OrderByModel orderByModel, String... columns) {
        QueryWrapper<T> wrapper = addCondition(column, conditionEnum, val);
        wrapper.select(columns);
        if (orderByModel.getConditionEnum() == ConditionEnum.ASC) {
            wrapper.orderByAsc(orderByModel.getColumns());
        } else {
            wrapper.orderByDesc(orderByModel.getColumns());
        }
        return this.listMaps(wrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps(String column, ConditionEnum conditionEnum, Object val, List<OrderByModel> orderByModels, String... columns) {
        QueryWrapper<T> wrapper = addCondition(column, conditionEnum, val);
        wrapper.select(columns);
        for (OrderByModel ob : orderByModels) {
            if (ob.getConditionEnum() == ConditionEnum.ASC) {
                wrapper.orderByAsc(ob.getColumns());
            } else {
                wrapper.orderByDesc(ob.getColumns());
            }
        }
        return this.listMaps(wrapper);
    }

    private QueryWrapper<T> addCondition(String column, ConditionEnum conditionEnum, Object val) {
        BaseQuery<T> queryWrapper = new BaseQuery<>();
        switch (conditionEnum) {
            case EQ:
                queryWrapper.eq(column, val);
                break;

            case NE:
                queryWrapper.ne(column, val);
                break;

            case GT:
                queryWrapper.gt(column, val);
                break;

            case GE:
                queryWrapper.ge(column, val);
                break;

            case LT:
                queryWrapper.lt(column, val);
                break;

            case LE:
                queryWrapper.le(column, val);
                break;

            case LIKE_ANY:
                queryWrapper.like(column, val);
                break;

            case LIKE_RIGHT:
                queryWrapper.likeRight(column, val);
                break;

            case LIKE_LEFT:
                queryWrapper.likeLeft(column, val);
                break;

            case NOT_LIKE_ANY:
                queryWrapper.notLike(column, val);
                break;

            case NOT_LIKE_RIGHT:
                queryWrapper.notLikeRight(column, val);
                break;

            case NOT_LIKE_LEFT:
                queryWrapper.notLikeLeft(column, val);
                break;

            case BETWEEN:
                Map<String, Object> between = BeanUtil.beanToMap(val);
                queryWrapper.between(column, between.get(ARG1), between.get(ARG2));
                break;

            case NOT_BETWEEN:
                Map<String, Object> notBetween = BeanUtil.beanToMap(val);
                queryWrapper.notBetween(column, notBetween.get(ARG1), notBetween.get(ARG2));
                break;

            case IS_NULL:
                queryWrapper.isNull(column);
                break;

            case IS_BLANK:
                queryWrapper.isNull(column).or(i -> i.eq(column, EMPTY));
                break;

            case IS_NOT_NULL:
                queryWrapper.isNotNull(column);
                break;

            case IS_NOT_BLANK:
                queryWrapper.isNotNull(column).ne(column, EMPTY);
                break;

            case IN:
                Collection<?> inColl = (Collection<?>) val;
                queryWrapper.in(column, inColl);
                break;

            case NOT_IN:
                Collection<?> notInColl = (Collection<?>) val;
                queryWrapper.notIn(column, notInColl);
                break;

            default:
                break;
        }

        return queryWrapper;
    }

    private String underlineToCamel(String param) {
        if (param == null) {
            return EMPTY;
        }

        String temp = param.toLowerCase();
        int len = temp.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = temp.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(temp.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
