package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class Company {

    private CompanyId id;
    private String name;
    private String registrationNumber;
    private Optional<RiskAssessment> riskAssessment;


    public void applyRiskAssessment(RiskAssessment riskAssessment){
        this.riskAssessment = Optional.of(riskAssessment);
    }
}
