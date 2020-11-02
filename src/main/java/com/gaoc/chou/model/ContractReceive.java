package com.gaoc.chou.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 合同领用表
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContractReceive extends Model<ContractReceive> {

    private static final long serialVersionUID = 1L;

    /**
     * ID，唯一标识
     */
    private String id;

    /**
     * 协议编号甲
     */
    private String treatyNoA;

    /**
     * 协议编号甲
     */
    private String treatyNoB;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 领用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDate receiveTime;

    /**
     * 合同模板ID
     */
    @NotBlank(message = "请选择合同模板")
    private String modelId;

    /**
     * 合同名称
     */
    @TableField(exist = false)
    private String modelName;

    /**
     * 协议模板ID
     */
    @NotBlank(message = "请选择协议模板")
    private String treatyId;

    /**
     * 类型模板ID
     */
    @NotBlank(message = "请选择合同类型")
    private String typeId;

    /**
     * 合同类型名称
     */
    @TableField(exist = false)
    private String typeName;

    /**
     * 领用部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 领用岗位ID
     */
    private String postId;

    /**
     * 岗位名称
     */
    @TableField(exist = false)
    private String postName;

    /**
     * 领用人ID
     */
    private String empId;

    /**
     * 领用人名称
     */
    @TableField(exist = false)
    private String empName;

    /**
     * 状态：0-作废，1-归档，2-领用，3-签订
     */
    private Integer status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建人ID
     */
    private String createId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改人ID
     */
    private String updateId;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
