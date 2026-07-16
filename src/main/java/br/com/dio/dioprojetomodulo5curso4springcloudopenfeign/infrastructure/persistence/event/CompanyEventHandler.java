package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.persistence.event;

import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.persistence.entity.CompanyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class CompanyEventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CompanyEventHandler.class);

    @HandleAfterCreate
    public void handleAfterCreateEvent(CompanyEntity entity) {
        LOG.info("HandleAfterCreateEvent {}", entity);
    }

}
