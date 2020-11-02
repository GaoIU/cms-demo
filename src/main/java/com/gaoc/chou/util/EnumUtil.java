package com.gaoc.chou.util;

import com.gaoc.constant.BaseConstant;

/**
 * @Description: 获取对应中文显示
 * @Date: 2020/7/8 22:30
 * @Author: Gaoc
 */
public class EnumUtil {

    private EnumUtil() {
    }

    public static String sysResourceType(Integer type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case BaseConstant.RESOURCE_TYPE_MENU:
                return "菜单";
            case BaseConstant.RESOURCE_TYPE_BTN:
                return "按钮";
            case BaseConstant.RESOURCE_TYPE_FUN:
                return "功能";
            default:
                return "未知";
        }
    }

    public static String intStatus(Integer status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case BaseConstant.INT_FALSE:
                return "禁用";
            case BaseConstant.INT_TRUE:
                return "正常";
            default:
                return "未知";
        }
    }

    public static String userStatus(Integer status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case BaseConstant.INT_FALSE:
                return "离职";
            case BaseConstant.INT_TRUE:
                return "在职";
            default:
                return "未知";
        }
    }

    public static String userSex(Integer sex) {
        if (sex == null) {
            return null;
        }

        switch (sex) {
            case BaseConstant.INT_FALSE:
                return "女";
            case BaseConstant.INT_TRUE:
                return "男";
            default:
                return "未知";
        }
    }

    public static String obCustomerState(Integer customerState) {
        if (customerState == null) {
            return null;
        }

        switch (customerState) {
            case 1:
                return "产值可签单";
            case 2:
                return "产值已出";
            case 3:
                return "定金可转换";
            case 4:
                return "跟进中";
            case 5:
                return "无效";
            case 6:
                return "已退单";
            case 7:
                return "已完结";
            case 8:
                return "意向可签单";
            default:
                return "未知";
        }
    }

    public static String obTreatyType(Integer treatyType) {
        if (treatyType == null) {
            return null;
        }

        switch (treatyType) {
            case 1:
                return "产品代购";
            case 2:
                return "产品代购/合作";
            case 3:
                return "产品合作";
            case 4:
                return "定金协议";
            case 5:
                return "设计协议";
            default:
                return "未知";
        }
    }

    public static String obTreatyDetail(Integer treatyDetail) {
        if (treatyDetail == null) {
            return null;
        }

        switch (treatyDetail) {
            case 1:
                return "可退";
            case 2:
                return "不可退";
            case 3:
                return "纯设计";
            case 4:
                return "全案设计";
            case 5:
                return "返点";
            case 6:
                return "全款";
            case 7:
                return "全款/返点";
            default:
                return "未知";
        }
    }

    public static String obHardcover(Integer hardcover) {
        if (hardcover == null) {
            return null;
        }

        switch (hardcover) {
            case 0:
                return "否";
            case 1:
                return "是";
            default:
                return "未知";
        }
    }

}
