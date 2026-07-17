package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.rest.client;

import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.rest.dto.SanctionResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "sanction-client", fallback = SanctionClient.class)
public interface SanctionClient {

    @GetMapping("/sanctions/companies/{registrationNumber}")
    SanctionResult getCompanyRisk(@PathVariable String registrationNumber);

    @Component
    class Fallback implements SanctionClient{
        public SanctionResult getCompanyRisk(String registrationNumber) {
            return new SanctionResult(List.of());
        }
    }
}