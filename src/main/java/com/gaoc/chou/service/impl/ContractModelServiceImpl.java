package com.gaoc.chou.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gaoc.chou.mapper.ContractModelMapper;
import com.gaoc.chou.model.ContractModel;
import com.gaoc.chou.model.FileDetail;
import com.gaoc.chou.service.IContractModelService;
import com.gaoc.chou.service.IFileDetailService;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.model.R;
import com.gaoc.core.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同模板表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-18
 */
@RequiredArgsConstructor
@Service
public class ContractModelServiceImpl extends BaseServiceImpl<ContractModelMapper, ContractModel> implements IContractModelService {

    private final IFileDetailService fileDetailService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R saveContractModel(ContractModel contractModel) {
        String fileId = contractModel.getFileId();
        if (StrUtil.isBlank(fileId)) {
            return R.fail("请上传合同模板文件");
        }
        boolean exist = fileDetailService.isExist("id", ConditionEnum.EQ, fileId);
        if (!exist) {
            return R.fail("请上传合同模板文件");
        }

        boolean save = save(contractModel);
        if (save) {
            FileDetail fileDetail = new FileDetail();
            fileDetail.setId(contractModel.getFileId());
            fileDetail.setStatus(BaseConstant.INT_TRUE);
            fileDetailService.updateById(fileDetail);

            return R.ok();
        }

        return R.fail("操作失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R updateContractModel(ContractModel contractModel) {
        ContractModel model = getOne("id", ConditionEnum.EQ, contractModel.getId(), "id", "file_id");
        if (StrUtil.isBlank(contractModel.getFileId())) {
            contractModel.setFileId(null);
        }

        boolean update = updateById(contractModel);
        if (update) {
            String fileId = contractModel.getFileId();
            if (StrUtil.isNotBlank(fileId)) {
                String oldFileId = model.getFileId();
                FileDetail oldFileDetail = new FileDetail();
                oldFileDetail.setId(oldFileId);
                oldFileDetail.setStatus(BaseConstant.INT_FALSE);
                fileDetailService.updateById(oldFileDetail);

                boolean exist = fileDetailService.isExist("id", ConditionEnum.EQ, fileId);
                if (exist) {
                    FileDetail fileDetail = new FileDetail();
                    fileDetail.setId(fileId);
                    fileDetail.setStatus(BaseConstant.INT_TRUE);
                    fileDetailService.updateById(fileDetail);
                }
            }

            return R.ok();
        }

        return R.fail("操作失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R removeContractModel(List<String> idList) {
        List<ContractModel> list = list("id", ConditionEnum.IN, idList, "id", "file_id");
        if (CollUtil.isEmpty(list)) {
            return R.fail("操作数据不存在，请刷新页面重试");
        }

        boolean remove = removeByIds(idList);
        if (remove) {
            Set<String> fileIds = list.stream().map(ContractModel::getFileId).collect(Collectors.toSet());
            fileDetailService.updateFileStatus(BaseConstant.INT_FALSE, fileIds);

            return R.ok();
        }

        return R.fail("操作失败");
    }

}
