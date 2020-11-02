package com.gaoc.sys.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 后台角色表
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 开发主键
     */
    private String id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Length(max = 32, message = "角色名称不能超过32位长度")
    private String name;

    /**
     * 角色CODE
     */
    @NotBlank(message = "角色CODE不能为空")
    @Length(max = 32, message = "角色CODE不能超过32位长度")
    private String code;

    /**
     * 后台资源Id
     */
    @Size(min = 1, message = "请选择角色资源")
    @TableField(exist = false)
    private List<String> sysResourceIds;

    /**
     * 角色状态：0-禁用，1-正常
     */
    @NotNull(message = "请选择角色状态")
    private Integer status;

    /**
     * 角色状态中文显示
     */
    @TableField(exist = false)
    private String statusDesc;

    /**
     * 创建人ID
     */
    private String createId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改人ID
     */
    private String updateId;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
