package com.gaoc.chou.service;

import com.gaoc.chou.model.ContractModel;
import com.gaoc.core.model.R;
import com.gaoc.core.service.BaseService;

import java.util.List;

/**
 * <p>
 * 合同模板表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-18
 */
public interface IContractModelService extends BaseService<ContractModel> {

    R saveContractModel(ContractModel contractModel);

    R updateContractModel(ContractModel contractModel);

    R removeContractModel(List<String> idList);
}
