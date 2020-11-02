package com.gaoc.chou.service.impl;

import com.gaoc.chou.model.FileDetail;
import com.gaoc.chou.mapper.FileDetailMapper;
import com.gaoc.chou.service.IFileDetailService;
import com.gaoc.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * <p>
 * 文件上传明细表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-17
 */
@Service
public class FileDetailServiceImpl extends BaseServiceImpl<FileDetailMapper, FileDetail> implements IFileDetailService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFileStatus(Integer status, Set<String> fileIds) {
        baseMapper.updateFileStatus(status, fileIds);
    }
}
