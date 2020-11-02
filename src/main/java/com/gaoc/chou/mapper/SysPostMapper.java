package com.gaoc.chou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaoc.chou.model.SysPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 岗位信息表 Mapper 接口
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
public interface SysPostMapper extends BaseMapper<SysPost> {

    void updatePostNullByDeptIds(@Param("deptIds") List<String> idList);

}
