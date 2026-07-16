package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.persistence.repository;

import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "companies")
public interface CompanyEntityRepository extends CrudRepository<CompanyEntity, UUID> {
}
