package com.gaoc.chou.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户洽谈表
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderBasic extends Model<OrderBasic> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，唯一标识
     */
    private String id;

    @NotNull(message = "派单序号不能为空")
    private Integer talkNo;

    /**
     * 客户姓名
     */
    @NotBlank(message = "客户姓名不能为空")
    @Length(max = 32, message = "客户姓名长度不能超过32位")
    private String name;

    /**
     * 性别：0-女，1-男
     */
    @NotNull(message = "请选择性别")
    private Integer sex;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 洽谈日期
     */
    @NotNull(message = "请输入派单日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate talkTime;

    /**
     * 客户情况：1-产值可签单，2-产值已出，3-定金可转换，4-跟进中，5-无效，6-已退单，7-已完结，8-意向可签单
     */
    @NotNull(message = "请选择客户情况")
    private Integer customerState;

    /**
     * 客户情况文字描述
     */
    @TableField(exist = false)
    private String customerStateDesc;

    /**
     * 协议类型：1-产品代购，2-产品代购/合作，3-产品合作，4-定金协议，5-设计协议
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer treatyType;

    /**
     * 协议类型文字描述
     */
    @TableField(exist = false)
    private String treatyTypeDesc;

    /**
     * 协议详情：1-可退，2-不可退，3-纯设计，4-全案设计，5-返点，6-全款，7-全款/返点
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer treatyDetail;

    /**
     * 协议详情文字描述
     */
    @TableField(exist = false)
    private String treatyDetailDesc;

    /**
     * 设计签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDate designTime;

    /**
     * 产值签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDate outputTime;

    /**
     * 是否精装：0-否，1-是
     */
    private Integer hardcover;

    /**
     * 是否精装文字描述
     */
    @TableField(exist = false)
    private String hardcoverDesc;

    /**
     * 楼盘名称
     */
    @Length(max = 32, message = "楼盘名称长度不能超过32位")
    private String saleName;

    /**
     * 房号
     */
    private String saleNo;

    /**
     * 面积
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal saleArea;

    /**
     * 常驻人口
     */
    @Min(value = 1L, message = "常驻人口不能小于1")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer residentNum;

    /**
     * 意向风格
     */
    @Length(max = 200, message = "意向风格长度不能超过200位长度")
    private String purpose;

    /**
     * 费用预算最小
     */
    @Min(value = 0L, message = "费用预算不能低于0")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal salePriceMin;

    /**
     * 费用预算最大
     */
    @Min(value = 0L, message = "费用预算不能低于0")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal salePriceMax;

    /**
     * 费用预算文字描述
     */
    @TableField(exist = false)
    private String salePriceDesc;

    /**
     * 特殊需求
     */
    @Length(max = 200, message = "特殊需求长度不能超过200位长度")
    private String specialNeed;

    /**
     * 洽谈地点
     */
    @Length(max = 125, message = "洽谈地点长度不能超过125位")
    private String address;

    /**
     * 渠道
     */
    @Length(max = 200, message = "渠道长度不能超过200位")
    private String channel;

    /**
     * 推荐人
     */
    private String recommend;

    /**
     * 推荐人姓名
     */
    @TableField(exist = false)
    private String recommendName;

    /**
     * 营销中心部门ID
     */
    private String marketDept;

    /**
     * 营销中心部门名称
     */
    @TableField(exist = false)
    private String marketDeptName;

    /**
     * 营销中心人员ID
     */
    private String marketEmp;

    /**
     * 营销中心人员姓名
     */
    @TableField(exist = false)
    private String marketEmpName;

    /**
     * 营销负责人
     */
    private String marketBelong;

    /**
     * 营销负责人姓名
     */
    @TableField(exist = false)
    private String marketBelongName;

    /**
     * 设计中心部门ID
     */
    private String designDept;

    /**
     * 设计中心部门名称
     */
    @TableField(exist = false)
    private String designDeptName;

    /**
     * 设计中心人员ID
     */
    private String designEmp;

    /**
     * 设计中心人员姓名
     */
    @TableField(exist = false)
    private String designEmpName;

    /**
     * 设计负责人
     */
    private String designBelong;

    /**
     * 设计负责人姓名
     */
    @TableField(exist = false)
    private String designBelongName;

    /**
     * 客户经理
     */
    private String manager;

    /**
     * 客户经理姓名
     */
    @TableField(exist = false)
    private String managerName;

    /**
     * 备注
     */
    @Length(max = 200, message = "备注长度不能超过200位")
    private String remarks;

    /**
     * 是否生成二见：0-否，1-是
     */
    private Integer status;

    /**
     * 生成洽谈时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime createTalkTime;

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
