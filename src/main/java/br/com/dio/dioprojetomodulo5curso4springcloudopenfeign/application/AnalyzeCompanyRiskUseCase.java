package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.application;

import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.domain.Company;
import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.rest.client.SanctionClient;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeCompanyRiskUseCase {
    private final SanctionClient sanctionClient;


    public AnalyzeCompanyRiskUseCase(SanctionClient sanctionClient){
        this.sanctionClient = sanctionClient;
    }

    public void execute(Company domain) {
        sanctionClient.getCompanyRisk((domain.getRegistrationNumber()));

        // KYC -> San
        // AML ->

    }
}
