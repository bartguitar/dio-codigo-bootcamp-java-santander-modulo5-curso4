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