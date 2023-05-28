package com.bka.gpstracker.service;

import com.bka.gpstracker.entity.Enterprise;
import com.bka.gpstracker.entity.UserInfo;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.request.RegisterEnterpriseRequest;
import com.bka.gpstracker.model.response.UserResponse;
import com.bka.gpstracker.repository.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnterpriseService {

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    public Enterprise registerEnterprise(RegisterEnterpriseRequest request) {
        if (enterpriseRepository.findById(request.getEnterpriseCode()).isPresent())
            throw new TrackerAppException(ErrorCode.ENTERPRISE_EXIST);
        return enterpriseRepository.save(request.toEnterprise());
    }

    public List<Enterprise> getAll(int pageIndex, int pageSize) {
        Pageable paging = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Enterprise> userPage = enterpriseRepository.findAll(paging);
        return userPage.getContent();
    }

    public Enterprise getByEnterpriseCode(String enterpriseCode) {
        Optional<Enterprise> enterpriseOpt = enterpriseRepository.findById(enterpriseCode);
        if (enterpriseOpt.isPresent()) {
            return enterpriseOpt.get();
        } else
            throw new TrackerAppException(ErrorCode.ENTERPRISE_NOT_FOUND);
    }

    public Optional<Enterprise> findByEnterpriseCode(String enterpriseCode) {
        return enterpriseRepository.findById(enterpriseCode);
    }
}
