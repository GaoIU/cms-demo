package com.gaoc.chou.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 洽谈明细表
 * </p>
 *
 * @author Gaoc
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TalkDetail extends Model<TalkDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，唯一标识
     */
    private String id;

    /**
     * 邀约人员ID
     */
    @NotBlank(message = "请选择邀约人员")
    private String userId;

    /**
     * 邀约人员姓名
     */
    @TableField(exist = false)
    private String name;

    /**
     * 洽谈日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    @NotNull(message = "请选择洽谈日期")
    private LocalDate rateTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 洽谈ID
     */
    @NotBlank(message = "洽谈ID不能为空")
    private String talkId;

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
