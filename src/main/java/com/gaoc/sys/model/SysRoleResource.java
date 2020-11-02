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
 * 后台角色 - 后台资源 关联表
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRoleResource extends Model<SysRoleResource> {

    private static final long serialVersionUID = 1L;

    /**
     * 开发主键
     */
    private String id;

    /**
     * 后台角色ID
     */
    private String sysRoleId;

    /**
     * 后台资源ID
     */
    private String sysResourceId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public SysRoleResource(String sysRoleId, String sysResourceId) {
        this.sysRoleId = sysRoleId;
        this.sysResourceId = sysResourceId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
