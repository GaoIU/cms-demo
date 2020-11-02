package com.gaoc.core.enums;

public enum ConditionEnum {

	/**
	 * 等于
	 */
	EQ,

	/**
	 * 不等于
	 */
	NE,

	/**
	 * 大于
	 */
	GT,

	/**
	 * 大于等于
	 */
	GE,

	/**
	 * 小于
	 */
	LT,

	/**
	 * 小于等于
	 */
	LE,

	/**
	 * 全部模糊
	 */
	LIKE_ANY,

	/**
	 * 右模糊
	 */
	LIKE_RIGHT,

	/**
	 * 左模糊
	 */
	LIKE_LEFT,

	/**
	 * 不模糊
	 */
	NOT_LIKE_ANY,

	/**
	 * 不右模糊
	 */
	NOT_LIKE_RIGHT,

	/**
	 * 不左模糊
	 */
	NOT_LIKE_LEFT,

	/**
	 * 在...之间，条件值为Map对象，key为："arg1", "arg2"
	 */
	BETWEEN,

	/**
	 * 不在...之间，条件值为Map对象，key为："arg1", "arg2"
	 */
	NOT_BETWEEN,

	/**
	 * 为空
	 */
	IS_NULL,

	/**
	 * 为空，包含空字符串""
	 */
	IS_BLANK,

	/**
	 * 不为空
	 */
	IS_NOT_NULL,

	/**
	 * 不为空，包含空字符串""
	 */
	IS_NOT_BLANK,

	/**
	 * 在...里面，条件值为Collection对象
	 */
	IN,

	/**
	 * 不在...里面，条件值为Collection对象
	 */
	NOT_IN,

	/**
	 * 正序排序
	 */
	ASC,

	/**
	 * 倒序排序
	 */
	DESC;

}
