package com.gaoc.chou.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoc.chou.mapper.ContractReceiveMapper;
import com.gaoc.chou.model.Company;
import com.gaoc.chou.model.ContractModel;
import com.gaoc.chou.model.ContractReceive;
import com.gaoc.chou.model.TreatyModel;
import com.gaoc.chou.service.ICompanyService;
import com.gaoc.chou.service.IContractModelService;
import com.gaoc.chou.service.IContractReceiveService;
import com.gaoc.chou.service.ITreatyModelService;
import com.gaoc.core.enums.ConditionEnum;
import com.gaoc.core.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 合同领用表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-07
 */
@RequiredArgsConstructor
@Service
public class ContractReceiveServiceImpl extends BaseServiceImpl<ContractReceiveMapper, ContractReceive> implements IContractReceiveService {

    private final ITreatyModelService treatyModelService;

    private final IContractModelService contractModelService;

    private final ICompanyService companyService;

    @Override
    public synchronized List<String> treatyNoSign(String treatyId) {
        if (StrUtil.isBlank(treatyId)) {
            return null;
        }
        TreatyModel treatyModel = treatyModelService.getById(treatyId);
        if (treatyModel == null) {
            return null;
        }

        QueryWrapper<ContractReceive> wrapper = new QueryWrapper<>();
        LocalDate now = LocalDate.now();
        wrapper.eq("treaty_id", treatyId).eq("receive_time", now.toString());
        int count = count(wrapper);
        String noA = null;
        String noB = null;
        if (count == 0) {
            noA = "001";
            noB = "002";
        } else {
            String countStrA = String.valueOf(count + 1);
            String countStrB = String.valueOf(count + 2);

            int lengthA = countStrA.length();
            if (lengthA == 1) {
                noA = "00" + countStrA;
            } else if (lengthA == 2) {
                noA = "0" + countStrA;
            } else {
                noA = countStrA;
            }

            int lengthB = countStrB.length();
            if (lengthB == 1) {
                noB = "00" + countStrB;
            } else if (lengthB == 2) {
                noB = "0" + countStrB;
            } else {
                noB = countStrB;
            }
        }

        StringBuilder sbA = new StringBuilder();
        sbA.append(treatyModel.getCode()).append("-");
        sbA.append(now.getYear()).append(treatyModel.getCodeNo()).append(noA);

        StringBuilder sbB = new StringBuilder();
        sbB.append(treatyModel.getCode()).append("-");
        sbB.append(now.getYear()).append(treatyModel.getCodeNo()).append(noB);

        return Arrays.asList(sbA.toString(), sbB.toString());
    }

    @Override
    public synchronized String contractNoSign(String modelId) {
        if (StrUtil.isBlank(modelId)) {
            return null;
        }
        ContractModel contractModel = contractModelService.getById(modelId);
        if (contractModel == null) {
            return null;
        }
        Company company = companyService.getOne("id", ConditionEnum.EQ, contractModel.getCompanyId(), "code");
        if (company == null) {
            return null;
        }

        QueryWrapper<ContractReceive> wrapper = new QueryWrapper<>();
        LocalDate now = LocalDate.now();
        wrapper.eq("model_id", modelId).eq("receive_time", now.toString());
        int count = count(wrapper);
        String no = null;
        if (count == 0) {
            no = "001";
        } else {
            String countStr = String.valueOf(count + 1);
            int length = countStr.length();
            if (length == 1) {
                no = "00" + countStr;
            } else if (length == 2) {
                no = "0" + countStr;
            } else {
                no = countStr;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("(").append(now.getYear()).append(")").append(company.getCode()).append("-");
        sb.append(contractModel.getCode()).append("-");
        sb.append(now.format(DateTimeFormatter.ofPattern("MMdd"))).append(no);

        return sb.toString();
    }

}
