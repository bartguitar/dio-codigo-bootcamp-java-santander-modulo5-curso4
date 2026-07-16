package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.persistence.repository;

import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.domain.Company;
import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.domain.CompanyRepository;
import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCompanyRepository implements CompanyRepository {
    private final CompanyEntityRepository repository;

    public InMemoryCompanyRepository(CompanyEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Company company) {
        var entity = CompanyEntity.from(company);
        repository.save(entity);
    }
}