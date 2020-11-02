package com.gaoc.chou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaoc.chou.model.OrderBasic;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户洽谈表 Mapper 接口
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-31
 */
public interface OrderBasicMapper extends BaseMapper<OrderBasic> {

    void talk(@Param("id") String id, @Param("status") Integer status);

}
