package com.gaoc.chou.model;

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
 * 协议模板表
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TreatyModel extends Model<TreatyModel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，唯一标识
     */
    private String id;

    /**
     * 协议名称
     */
    @NotBlank(message = "协议名称不能为空")
    @Length(max = 32, message = "协议名称不能超过32位长度")
    private String name;

    /**
     * 协议编码
     */
    @NotBlank(message = "协议编码不能为空")
    @Length(max = 16, message = "协议编码不能超过16位长度")
    private String code;

    /**
     * 协议编号
     */
    @NotBlank(message = "协议编号不能为空")
    @Length(max = 16, message = "协议编号不能超过16位长度")
    private String codeNo;

    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空")
    private Integer sort;

    /**
     * 协议状态：0-禁用，1-启用
     */
    @NotNull(message = "请选择协议状态")
    private Integer status;

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
