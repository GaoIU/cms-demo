package com.gaoc.chou.service;

import com.gaoc.chou.model.ContractReceive;
import com.gaoc.core.service.BaseService;

import java.util.List;

/**
 * <p>
 * 合同领用表 服务类
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-07
 */
public interface IContractReceiveService extends BaseService<ContractReceive> {

    List<String> treatyNoSign(String treatyId);

    String contractNoSign(String modelId);

}
