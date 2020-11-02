package com.gaoc.chou.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 公司信息表
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Company extends Model<Company> {

    private static final long serialVersionUID = 1L;

    /**
     * 开发主键
     */
    private String id;

    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不能为空")
    @Length(max = 64, message = "公司名称不能超过64位长度")
    private String name;

    /**
     * 公司CODE
     */
    @NotBlank(message = "公司CODE不能为空")
    @Length(max = 32, message = "公司CODE不能超过32位长度")
    private String code;

    /**
     * 公司抬头
     */
    @NotBlank(message = "公司抬头不能为空")
    @Length(max = 64, message = "公司抬头不能超过64位长度")
    private String rise;

    /**
     * 公司税号
     */
    @NotBlank(message = "公司税号不能为空")
    @Length(max = 64, message = "公司税号不能超过64位长度")
    private String dutyNo;

    /**
     * 银行账号
     */
    @NotBlank(message = "银行账号不能为空")
    @Length(min = 19, max = 19, message = "银行账号不合法")
    private String bankNo;

    /**
     * 开户银行
     */
    @NotBlank(message = "开户银行不能为空")
    @Length(max = 32, message = "开户银行不能超过32位长度")
    private String bankName;

    /**
     * 公司地址
     */
    @NotBlank(message = "公司地址不能为空")
    @Length(max = 256, message = "公司地址不能超过256位长度")
    private String address;

    /**
     * 联系方式，JSON格式，key：name，mobile
     */
    private String touch;

    /**
     * 联系方式
     */
    @TableField(exist = false)
    private List<JSONObject> touchs;

    /**
     * 公司状态：0-禁用，1-正常
     */
    @NotNull(message = "请选择公司状态")
    private Integer status;

    /**
     * 公司状态文字显示
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
