package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sanction-client")
public interface SanctionClient {

    @GetMapping("/sanctions/companies/{registrationNumber}")
    void getCompanyRisk(@PathVariable String registrationNumber);
}
