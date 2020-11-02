package com.gaoc.constant;

/**
 * @Description: 全局常量
 * @Date: 2020/7/1 14:30
 * @Author: Gaoc
 */
public class BaseConstant {

    /**
     * 数字状态：1
     */
    public static final int INT_TRUE = 1;

    /**
     * 数字状态：0
     */
    public static final int INT_FALSE = 0;

    /**
     * spring security session key
     */
    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    /**
     * session key
     */
    public static final String DEFAULT_SESSION_KEY = "SYS_USER_SESSION";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 默认超级管理员角色CODE
     */
    public static final String DEFAULT_ROLE_CODE = "ADMINISTRATOR";

    /**
     * 资源redis缓存key
     */
    public static final String REDIS_SYS_RESOURCE = "sys-resource:";

    /**
     * 菜单redis缓存key
     */
    public static final String REDIS_MENU = "index-menu";

    /**
     * 菜单类型：1-菜单
     */
    public static final int RESOURCE_TYPE_MENU = 1;

    /**
     * 菜单类型：2-按钮
     */
    public static final int RESOURCE_TYPE_BTN = 2;

    /**
     * 菜单类型：3-功能
     */
    public static final int RESOURCE_TYPE_FUN = 3;

}
