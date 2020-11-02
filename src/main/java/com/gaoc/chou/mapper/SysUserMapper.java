package com.gaoc.chou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaoc.chou.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工信息表 Mapper 接口
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<Map<String, Object>> allUser(@Param("name") String name, @Param("type") Integer type, IPage<Map<String, Object>> page);

    void updateUserNullByPostIds(@Param("postIds") List<String> idList);

    void updateUserNullByDeptIds(@Param("deptIds") List<String> idList);

}
