## Sobre - Projeto

## Tecnologias

## Arquitetura e Estrutura de Pastas

### Seção 1 - Introdução
#### Configuração de Dependência inicial
- Não foi adicionada nenhuma dependência inicial para o projeto, somente as dependências padrões,
- irei adicionando assim que o projeto for evoluindo.

```
plugins {
    id 'java'
    id 'org.springframework.boot' version '4.1.0'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'br.com.dio'
version = '0.0.1-SNAPSHOT'
description = 'dio-projeto-modulo5-curso4-spring-cloud-openfeign'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
    useJUnitPlatform()
}
```
### Seção 2 - Setup do Projeto Compliance
- 2.1 - Criar as pastas "application"/"domain"/"infrastructure" \
- 2.2 - Adicionar plugin "lombok" \
- 2.3 - Adicionar 4 dependências "spring-data-keyvalue" / "rest" / "web" / "actuator" \
- 2.4 - Adicionar anotação "@EnableMapRepositories" \
- 2.5 - Criar classes de domínio - "Company" / classe record "CompanyId" / classe record "RiskAssessment" \
- 2.6 - Criar classes enum "RiskLevel" / enum "RiskAssessmentStatus" \
- 2.7 - Criar classe interface "CompanyRepository" \
- 2.8 - Criar classe record "ComplianceScreening" \
- 2.9 - Criar classe "CompliancePolicy" \
--Feito Commit--
### Seção 3 - Modelando empresas com Spring Data
3.1 - Criar pacotes "persistence" / "entity" / "repository" dentro de "infra...." \
3.2 - Criar entidade classe "CompanyEntity" \
3.3 - Criar classe interface "CompanyEntityRepository" \
3.4 - Testar aplicação em http://localhost:8080/ \
3.5 - Testar também http://localhost:8080/companies \
3.6 - Testar POST criar uma company \
3.7 - Criar classe "InMemoryCompanyRepository" \
3.8 - Alterar interface "CompanyRepository" alterar para "void" método "save" \
3.9 - Criar pacote "evento" classe "CompanyEventHandler" \
3.10 - Testar aplicação (Testar POST criar uma company) \
--Feito Commit--
### Seção 4 - Estruturando Use Cases
4.1 - Começar as regras de negócio - criar classe "AnalyzeCompanyRiskUseCase" \
4.2 - Alterar classe "CompanyEvent....." injetando classe "AnalyzeCompany....." \
--Feito Commit--
4.3 - Agora serão criadas as classes para interagir com "Spring OpenFeign" \
4.4 - Usar o mock para criar apis externas mocadas para interagir com a aplicação - https://mockoon.com/ ou postman/insomnia \
4.5 - Especificar variável "ext" no "build.gradle" chamada 'springCloudVersion' \
4.6 - Adicionar a dependência "spring-openfeign" no "build.gradle" \
4.7 - Adicionar "dependencyManagement" no "build.gradle" \
4.8 - Adicionar anotação "@EnableFeignClients" na classe de aplicação \
4.9 - Criar pacote "rest" dentro de infra e dentro dele criar o pacote "client" \
4.10 - Criar interface "SanctionClient" \
4.11 - Terminar de implementar classe "AnalyzeCompany...." \
4.12 - Testar se a aplicação subiu e testar no mock ou postman a api \
4.13 - Agora testar toda a aplicação, criando a empresa e depois com a api mockada externa \
4.14 - No código onde está "url = "http://192.168.64.1:3001" deverá ser substituido pelo endereço da api minha criada \
--Feito Commit--
### Seção 5 - Monitorando Requisições e Respostas
5.1 - Adicionar propriedades do openfeign no "application.properties" - "sanction-client-logger" / "sanction-client-url" \
5.2 - Alterar classe "SanctionClient" retirando a url e colocando em properties \
5.3 - Criar pacote "dto" e classe record "SanctionResult" \
5.4 - Testar aplicação na api, simular autorização no software de api escolhido \
5.5 - Adicionar propriedades do openfeign no "application.properties" - "sanction-client-default \
5.6 - Testar aplicação \
--Feito Commit--