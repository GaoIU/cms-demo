package com.gaoc.chou.service.impl;

import com.gaoc.chou.mapper.CompanyMapper;
import com.gaoc.chou.model.Company;
import com.gaoc.chou.service.ICompanyService;
import com.gaoc.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公司信息表 服务实现类
 * </p>
 *
 * @author Gaoc
 * @since 2020-07-01
 */
@Service
public class CompanyServiceImpl extends BaseServiceImpl<CompanyMapper, Company> implements ICompanyService {

}
