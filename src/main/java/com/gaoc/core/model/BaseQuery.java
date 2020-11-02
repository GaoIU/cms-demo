package com.gaoc.core.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@SuppressWarnings("serial")
public class BaseQuery<T> extends QueryWrapper<T> {

	public QueryWrapper<T> notLikeRight(String column, Object val) {
		return not(true).likeRight(column, val);
	}

	public QueryWrapper<T> notLikeLeft(String column, Object val) {
		return not(true).likeLeft(column, val);
	}

}
