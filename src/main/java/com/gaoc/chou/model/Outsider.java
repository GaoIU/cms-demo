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
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 外部人员表
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Outsider extends Model<Outsider> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，唯一标识
     */
    private String id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Length(max = 32, message = "名称长度不能超过32位")
    private String name;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "0?(13|14|15|18|17|19)[0-9]{9}", message = "手机号码不合法")
    private String mobile;

    /**
     * 状态：0-禁用，1-正常
     */
    @NotNull(message = "请选择外部人员状态")
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
