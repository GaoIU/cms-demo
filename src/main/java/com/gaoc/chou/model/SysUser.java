package com.gaoc.chou.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工信息表
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 开发主键
     */
    private String id;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Length(max = 32, message = "姓名长度不能超过32位长度")
    private String name;

    /**
     * 英文名称
     */
    @Length(max = 64, message = "英文名称长度不能超过64位长度")
    private String nickName;

    /**
     * 性别：0-女，1-男
     */
    @NotNull(message = "请选择性别")
    private Integer sex;

    /**
     * 性别文字显示
     */
    @TableField(exist = false)
    private String sexDesc;

    /**
     * 工号
     */
    @NotBlank(message = "工号不能为空")
    @Length(max = 16, message = "工号长度不能超过16位长度")
    private String jobNo;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "0?(13|14|15|18|17|19)[0-9]{9}", message = "手机号码不合法")
    private String mobile;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱不合法")
    private String email;

    /**
     * 身份证号码
     */
    @NotBlank(message = "身份证号码不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", message = "身份证号码不合法")
    private String cardNo;

    /**
     * 银行卡号
     */
    @NotBlank(message = "银行卡号不能为空")
    @Length(min = 19, max = 19, message = "银行卡号不合法")
    private String bankNo;

    /**
     * 银行名称
     */
    @NotBlank(message = "银行名称不能为空")
    @Length(max = 32, message = "银行名称不能为超过32位长度")
    private String bankName;

    /**
     * 办理地
     */
    @NotBlank(message = "办理地不能为空")
    @Length(max = 64, message = "办理地不能为超过64位长度")
    private String bankAddress;

    /**
     * 开户行名称
     */
    @NotBlank(message = "开户行名称不能为空")
    @Length(max = 64, message = "开户行名称不能为超过64位长度")
    private String bankAccountName;

    /**
     * 现任部门ID
     */
    @NotNull(message = "请选择现任所属部门")
    private String deptId;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 籍贯
     */
    @NotBlank(message = "籍贯不能为空")
    @Length(max = 64, message = "籍贯不能为超过64位长度")
    private String nativePlace;

    /**
     * 户别
     */
    @NotBlank(message = "户别不能为空")
    @Length(max = 8, message = "户别不能为超过8位长度")
    private String household;

    /**
     * 社保缴纳起始月份
     */
    @NotBlank(message = "请选择社保缴纳起始月份")
    @Length(max = 8, message = "社保缴纳起始月份不能为超过8位长度")
    private String socialStartTime;

    /**
     * 社保缴纳缴费基数
     */
    @NotNull(message = "社保缴纳缴费基数不能为空")
    @Min(value = 0, message = "社保缴纳缴费基数不能低于0")
    private BigDecimal socialPrice;

    /**
     * 公积金基数
     */
    @NotNull(message = "公积金基数不能为空")
    @Min(value = 0, message = "公积金基数不能低于0")
    private BigDecimal housingPrice;

    /**
     * 社保缴纳最后月份
     */
    @Length(max = 8, message = "社保缴纳最后月份不能为超过8位长度")
    private String socialEndTime;

    /**
     * 学历
     */
    @Length(max = 8, message = "学历不能为超过8位长度")
    private String education;

    /**
     * 毕业学校
     */
    @Length(max = 64, message = "毕业学校不能为超过64位长度")
    private String school;

    /**
     * 专业
     */
    @Length(max = 32, message = "专业不能为超过32位长度")
    private String major;

    /**
     * 入职时间
     */
    @NotNull(message = "请选择入职时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate entryTime;

    /**
     * 合同时间
     */
    @NotNull(message = "请选择合同时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate contractTime;

    /**
     * 入职部门ID
     */
    @NotBlank(message = "请选择入职部门")
    private String entryDeptId;

    /**
     * 入职部门名称
     */
    @TableField(exist = false)
    private String entryDeptName;

    /**
     * 入职岗位ID
     */
    @NotNull(message = "请选择入职岗位")
    private String entryPostId;

    /**
     * 入职岗位名称
     */
    @TableField(exist = false)
    private String entryPostName;

    /**
     * 现任岗位ID
     */
    @NotNull(message = "请选择现任岗位")
    private String postId;

    /**
     * 现任岗位名称
     */
    @TableField(exist = false)
    private String postName;

    /**
     * 入职薪资
     */
    @NotNull(message = "入职薪资不能为空")
    @Min(value = 0, message = "入职薪资不能低于0")
    private BigDecimal entrySalary;

    /**
     * 转正薪资
     */
    @NotNull(message = "转正薪资不能为空")
    @Min(value = 0, message = "转正薪资不能低于0")
    private BigDecimal workerSalary;

    /**
     * 实时薪资
     */
    @NotNull(message = "实时薪资不能为空")
    @Min(value = 0, message = "实时薪资不能低于0")
    private BigDecimal salary;

    /**
     * 绩效薪资
     */
    @NotNull(message = "绩效薪资不能为空")
    @Min(value = 0, message = "绩效薪资不能低于0")
    private BigDecimal meritsSalary;

    /**
     * 岗位薪资
     */
    @NotNull(message = "岗位薪资不能为空")
    @Min(value = 0, message = "岗位薪资不能低于0")
    private BigDecimal postSalary;

    /**
     * 转正日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate workerTime;

    /**
     * 离职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate quitTime;

    /**
     * 员工状态：0-离职，1-在职
     */
    @NotNull(message = "请选择员工状态")
    private Integer status;

    /**
     * 是否禁用：0-否，1-是
     */
    private Integer isDisable;

    /**
     * 员工状态文字显示
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
