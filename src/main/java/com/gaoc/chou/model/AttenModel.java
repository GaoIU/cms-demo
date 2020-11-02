package com.gaoc.chou.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 考勤模板表
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AttenModel extends Model<AttenModel> {

    private static final long serialVersionUID = 1L;

    /**
     * 开发主键
     */
    private String id;

    /**
     * 考勤模板名称
     */
    @NotBlank(message = "考勤模板名称不能为空")
    @Length(max = 16, message = "考勤模板名称长度不能超过16位长度")
    private String name;

    /**
     * 月份
     */
    @NotBlank(message = "月份不能为空")
    private String month;

    /**
     * 休息日，多个用逗号隔开
     */
    @NotBlank(message = "休息日不能为空")
    private String rest;

    /**
     * 休息日
     */
    @TableField(exist = false)
    private List<String> rests;

    /**
     * 假期，JSON格式数据
     */
    private String vacation;

    /**
     * 假期
     */
    @TableField(exist = false)
    private List<JSONObject> vacations;

    /**
     * 补假工作日，JSON格式数据
     */
    private String work;

    /**
     * 补假工作日
     */
    @TableField(exist = false)
    private List<JSONObject> works;

    /**
     * 是否生成考勤记录：0-未生成，1-已生成
     */
    private Integer status;

    /**
     * 生成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime generateTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private String createId;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
