package com.gaoc.chou.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Description: 员工后台角色分配
 * @Date: 2020/7/2 22:16
 * @Author: Gaoc
 */
@Data
public class UserRoleVO {

    @NotNull(message = "请选择要操作的员工")
    private String sysUserId;

    @Size(min = 1, message = "请至少分配一个后台角色")
    private List<String> sysRoleIds;

}
