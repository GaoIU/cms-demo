package com.gaoc.chou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaoc.chou.model.FileDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 文件上传明细表 Mapper 接口
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-17
 */
public interface FileDetailMapper extends BaseMapper<FileDetail> {

    void updateFileStatus(@Param("status") Integer status, @Param("fileIds") Set<String> fileIds);

}
