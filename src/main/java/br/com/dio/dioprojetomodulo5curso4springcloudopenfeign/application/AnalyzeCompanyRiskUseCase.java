package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.application;

import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.domain.Company;
import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.domain.CompanyRepository;
import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.domain.CompliancePolicy;
import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.domain.ComplianceScreening;
import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.rest.client.AntiMoneyLaunderingClient;
import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.rest.client.SanctionClient;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeCompanyRiskUseCase {
    private final SanctionClient sanctionClient;
    private final AntiMoneyLaunderingClient antiMoneyLaunderingClient;
    private final CompanyRepository companyRepository;


    public AnalyzeCompanyRiskUseCase(SanctionClient sanctionClient,
                                     AntiMoneyLaunderingClient antiMoneyLaunderingClient,
                                     CompanyRepository companyRepository){
        this.sanctionClient = sanctionClient;
        this.antiMoneyLaunderingClient = antiMoneyLaunderingClient;
        this.companyRepository = companyRepository;
    }

    public void execute(Company company) {
        var sanctions = sanctionClient.getCompanyRisk(company.getRegistrationNumber()).toDomain();
        var amlProfile = antiMoneyLaunderingClient.screening(company.getRegistrationNumber()).toDomain();

        var screening = new ComplianceScreening(sanctions, amlProfile);
        var riskAssessment = CompliancePolicy.evaluate(screening);

        company.applyRiskAssessment(riskAssessment);
        companyRepository.save(company);
    }
}
