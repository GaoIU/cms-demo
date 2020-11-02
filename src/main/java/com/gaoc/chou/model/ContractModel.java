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
 * 合同模板表
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContractModel extends Model<ContractModel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 合同模板名称
     */
    @NotBlank(message = "合同模板名称不能为空")
    @Length(max = 32, message = "合同模板名称不能超过32位长度")
    private String name;

    /**
     * 公司ID
     */
    @NotBlank(message = "请选择所属公司")
    private String companyId;

    /**
     * 公司名称
     */
    @TableField(exist = false)
    private String companyName;

    /**
     * 合同编码
     */
    @NotBlank(message = "合同编码不能为空")
    private String code;

    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空")
    private Integer sort;

    /**
     * 合同文件ID
     */
    private String fileId;

    /**
     * 合同文件名称
     */
    @NotBlank(message = "合同文件名称不能为空")
    private String fileName;

    /**
     * 合同文件地址
     */
    @NotBlank(message = "合同文件地址不能为空")
    private String fileUrl;

    /**
     * 合同状态：0-禁用，1-正常
     */
    @NotNull(message = "请选择合同状态")
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
