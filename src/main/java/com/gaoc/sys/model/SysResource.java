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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 后台资源表
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysResource extends Model<SysResource> {

    private static final long serialVersionUID = 1L;

    /**
     * 开发主键
     */
    private String id;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    @Length(max = 16, message = "资源名称不能超过16位长度")
    private String name;

    /**
     * 资源编码
     */
    @NotBlank(message = "资源编码不能为空")
    @Length(max = 32, message = "资源编码不能超过32位长度")
    private String code;

    /**
     * 资源地址
     */
    private String url;

    /**
     * 访问方式
     */
    @NotBlank(message = "访问方式不能为空")
    @Length(max = 16, message = "访问方式不能超过16位长度")
    private String method;

    /**
     * 资源类型：1-菜单，2-按钮，3-功能
     */
    @NotNull(message = "请选择资源类型")
    private Integer type;

    /**
     * 资源类型文字显示
     */
    @TableField(exist = false)
    private String typeDesc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 上级资源ID
     */
    private String parentId;

    /**
     * 上级资源名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 资源描述
     */
    @Length(max = 200, message = "资源描述不能超过200位长度")
    private String description;

    /**
     * 资源状态：0-禁用，1-正常
     */
    @NotNull(message = "请选择资源状态")
    private Integer status;

    /**
     * 资源状态文字显示
     */
    @TableField(exist = false)
    private String statusDesc;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 修改人ID
     */
    private String updateId;

    /**
     * 创建人ID
     */
    private String createId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
