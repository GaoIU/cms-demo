package com.gaoc.core.model;

import com.gaoc.core.enums.ConditionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderByModel {

	/**
	 * 排序方式
	 */
	private ConditionEnum conditionEnum;

	/**
	 * 排序字段
	 */
	private String[] columns;

}
