package com.gaoc.chou.service;

import com.gaoc.chou.model.FileDetail;
import com.gaoc.core.service.BaseService;

import java.util.Set;

/**
 * <p>
 * 文件上传明细表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-17
 */
public interface IFileDetailService extends BaseService<FileDetail> {

    void updateFileStatus(Integer status, Set<String> fileIds);

}
