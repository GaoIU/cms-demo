package com.gaoc.chou.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件上传明细表
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileDetail extends Model<FileDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，唯一标识
     */
    private String id;

    /**
     * 文件原始中文名称
     */
    private String name;

    /**
     * 存储桶名称
     */
    private String bucket;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件状态：0-临时文件，1-永久文件
     */
    private Integer status;

    /**
     * 上传人ID
     */
    private String createId;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public FileDetail(String name, String bucket, String fileName, String fileUrl, Long fileSize, String fileType, Integer status, String createId) {
        this.name = name;
        this.bucket = bucket;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.status = status;
        this.createId = createId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
