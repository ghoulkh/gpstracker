package com.bka.gpstracker.model.request;

import com.bka.gpstracker.entity.Enterprise;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class RegisterEnterpriseRequest {
    @NotBlank(message = "enterpriseCode is required!")
    private String enterpriseCode;
    @NotBlank(message = "enterpriseName is required!")
    private String enterpriseName;

    public Enterprise toEnterprise() {
        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseName(this.enterpriseName);
        enterprise.setEnterpriseCode(this.enterpriseCode);
        Date currentDate = new Date();
        enterprise.setCreatedDate(currentDate);
        enterprise.setLastUpdatedDate(currentDate);
        return enterprise;
    }
}
