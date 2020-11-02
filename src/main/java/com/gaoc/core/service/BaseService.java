package com.gaoc.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.OrderByModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T> extends IService<T> {

	boolean isExist(Wrapper<T> queryWrapper);

	boolean isExist(String column, ConditionEnum conditionEnum, Object val);

	T getOne(String column, ConditionEnum conditionEnum, Object val);

	T getOne(String column, ConditionEnum conditionEnum, Object val, String... columns);

	List<T> list(ConditionEnum conditionEnum, String... orderBys);

	List<T> list(List<OrderByModel> orderByModels);

	List<T> list(String column, ConditionEnum conditionEnum, Object val);

	List<T> list(String column, ConditionEnum conditionEnum, Object val, String... columns);

	List<T> list(String column, ConditionEnum conditionEnum, Object val, OrderByModel orderByModel, String... columns);

	List<T> list(String column, ConditionEnum conditionEnum, Object val, List<OrderByModel> orderByModels,
                 String... columns);

	List<T> list(String column, ConditionEnum conditionEnum, Object val, ConditionEnum orderBy, String... orderBys);

	int count(String column, ConditionEnum conditionEnum, Object val);

	boolean isOnly(Serializable id, String column, Object val);

	boolean isOnly(Serializable id, String idFiled, String column, Object val);

	boolean remove(String column, ConditionEnum conditionEnum, Object val);

	IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val);

	IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                  String... columns);

	IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                  ConditionEnum orderBy, String... orderBys);

	IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                  OrderByModel orderByModel, String... columns);

	IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                  List<OrderByModel> orderByModels, String... columns);

	IPage<T> page(Integer current, Integer size, String column, ConditionEnum conditionEnum, Object val,
                  List<OrderByModel> orderByModels);

	List<Map<String, Object>> listMaps(String column, ConditionEnum conditionEnum, Object val);

	List<Map<String, Object>> listMaps(String column, ConditionEnum conditionEnum, Object val, String... columns);

	List<Map<String, Object>> listMaps(String column, ConditionEnum conditionEnum, Object val, OrderByModel orderByModel, String... columns);

	List<Map<String, Object>> listMaps(String column, ConditionEnum conditionEnum, Object val, List<OrderByModel> orderByModels,
				 String... columns);

}
