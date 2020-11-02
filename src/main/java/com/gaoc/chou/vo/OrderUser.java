package com.gaoc.chou.vo;

import lombok.Data;

import java.util.Set;

/**
 * @Description: 派单获取所有用户VO
 * @Date: 2020/9/16 13:04
 * @Author: Gaoc
 */
@Data
public class OrderUser {

    private Integer current = 1;

    private Integer size = 10;

    private String name;

    private Integer type;

    private Set<String> ids;

}
