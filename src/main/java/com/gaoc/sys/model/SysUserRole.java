package com.gaoc.sys.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工用户 - 后台角色 关联表
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserRole extends Model<SysUserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 开发主键
     */
    private String id;

    /**
     * 员工用户ID
     */
    private String sysUserId;

    /**
     * 后台角色ID
     */
    private String sysRoleId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public SysUserRole(String sysUserId, String sysRoleId) {
        this.sysUserId = sysUserId;
        this.sysRoleId = sysRoleId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
