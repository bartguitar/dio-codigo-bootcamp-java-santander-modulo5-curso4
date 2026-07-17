# DIO Projeto Módulo 5 · Curso 4 — Spring Cloud OpenFeign

> Serviço de análise de risco de compliance (KYC/AML) para empresas, construído em Java 25 + Spring Boot, integrando APIs externas de sanções e prevenção à lavagem de dinheiro via **Spring Cloud OpenFeign**, com resiliência via **Resilience4j Circuit Breaker**.

Projeto desenvolvido durante o **Bootcamp Santander Back-End Java (DIO)**, módulo 5 · curso 4, com foco em consumo de APIs REST desacopladas usando Feign Clients, fallback e circuit breaker.

---

## 📖 Sobre o projeto

O serviço simula um fluxo de **onboarding de empresas (KYC — Know Your Customer)** em um contexto de compliance bancário/financeiro:

1. Uma empresa é cadastrada via API (`POST /companies`).
2. Assim que o registro é persistido, um *event handler* dispara automaticamente uma análise de risco.
3. Essa análise consulta **duas APIs externas** (mockadas) via Feign:
   - **Sanction API** — verifica se a empresa está presente em listas de sanções internacionais;
   - **AML API** — retorna um score de risco de lavagem de dinheiro e se há exposição política (PEP — *Politically Exposed Person*).
4. As respostas são combinadas por uma política de negócio (`CompliancePolicy`), que classifica a empresa em um nível de risco e um status de aprovação.
5. O resultado é persistido junto ao registro da empresa.

O projeto não tem interface gráfica — é consumido via API REST (Spring Data REST) e testado com ferramentas como Postman, Insomnia ou [Mockoon](https://mockoon.com/) para simular as APIs externas.

## 🧠 Regras de negócio (`CompliancePolicy`)

| Condição | Resultado |
|---|---|
| Alguma sanção com `confidence > 0.8` | Status `REJECTED` (nível `CRITICAL`) |
| Empresa é PEP (`isPepPresent = true`) e não há sanção crítica | Status `MANUAL_REVIEW` |
| Score AML (`riskScore`) > 70 e nenhuma das regras acima se aplica | Status `MANUAL_REVIEW` |
| Nenhuma das condições acima | Status `APPROVED` |

O **nível de risco** (`RiskLevel`) é derivado do score e do status finais:

| Score / Status | Nível |
|---|---|
| Status `REJECTED` | `CRITICAL` |
| Score > 70 | `HIGH` |
| Score > 30 | `MEDIUM` |
| Score ≤ 30 | `LOW` |

## 🏗️ Arquitetura

O código segue uma separação inspirada em **DDD leve (domain / application / infrastructure)**, mantendo as regras de negócio isoladas de detalhes de persistência e de integração HTTP:

```
src/main/java/br/com/dio/dioprojetomodulo5curso4springcloudopenfeign/
├── application/
│   └── AnalyzeCompanyRiskUseCase.java     # orquestra a análise de risco (caso de uso)
├── domain/
│   ├── Company.java                       # agregado raiz
│   ├── CompanyId.java                     # value object (UUID)
│   ├── CompanyRepository.java             # porta de saída (interface)
│   ├── CompliancePolicy.java              # regra de negócio pura
│   ├── ComplianceScreening.java           # dados combinados de sanções + AML
│   ├── RiskAssessment.java                # resultado da análise
│   ├── RiskAssessmentStatus.java          # APPROVED / REJECTED / MANUAL_REVIEW
│   └── RiskLevel.java                     # LOW / MEDIUM / HIGH / CRITICAL
└── infrastructure/
    ├── persistence/
    │   ├── entity/CompanyEntity.java             # entidade Spring Data
    │   ├── event/CompanyEventHandler.java        # dispara a análise após criação (@HandleAfterCreate)
    │   └── repository/
    │       ├── CompanyEntityRepository.java      # Spring Data REST (CrudRepository)
    │       └── InMemoryCompanyRepository.java    # adapta o repositório de domínio ao Spring Data
    └── rest/
        ├── client/
        │   ├── SanctionClient.java               # Feign Client + fallback
        │   └── AntiMoneyLaunderingClient.java     # Feign Client
        └── dto/
            ├── SanctionResult.java                # DTO da API de sanções (+ toDomain)
            └── AmlResult.java                     # DTO da API de AML/PEP (+ toDomain)
```

**Fluxo da requisição:**

```
POST /companies
      │
      ▼
CompanyEntityRepository (Spring Data REST)
      │  @HandleAfterCreate
      ▼
CompanyEventHandler
      │
      ▼
AnalyzeCompanyRiskUseCase.execute(company)
      │
      ├──► SanctionClient.getCompanyRisk()   ──► Sanction API (Feign, com fallback + circuit breaker)
      ├──► AntiMoneyLaunderingClient.screening() ──► AML API (Feign)
      │
      ▼
CompliancePolicy.evaluate(screening) ──► RiskAssessment
      │
      ▼
company.applyRiskAssessment(...) ──► CompanyRepository.save(company)
```

## 🚀 Tecnologias

| Categoria | Tecnologia | Uso no projeto |
|---|---|---|
| Linguagem | **Java 25** (toolchain via Gradle) | Linguagem principal do projeto |
| Framework | **Spring Boot 4.1.0** | Base da aplicação |
| Comunicação HTTP / Cliente REST | **Spring Cloud OpenFeign** | Clientes HTTP declarativos para as APIs de sanções e AML |
| Resiliência | **Spring Cloud Circuit Breaker (Resilience4j)** | Resiliência nas chamadas ao `SanctionClient` |
| Persistência / Exposição de API | **Spring Data REST** + **Spring Data KeyValue** (`@EnableMapRepositories`) | Repositório em memória exposto automaticamente como API REST |
| Observabilidade | **Spring Boot Actuator** | Endpoints de monitoramento e saúde da aplicação |
| Produtividade | **Lombok** (via plugin `io.freefair.lombok`) | Redução de boilerplate (getters, construtores) |
| Build | **Gradle** | Build e gerenciamento de dependências |
| Testes | **JUnit 5** | Testes automatizados |

## ⚙️ Configuração

As integrações Feign são configuradas em `src/main/resources/application.properties`:

```properties
spring.cloud.openfeign.client.config.sanction-client.url=http://192.168.64.1:3001
spring.cloud.openfeign.client.config.sanction-client.logger-level=full
spring.cloud.openfeign.client.config.sanction-client.default-request-headers.x-api-key=kyc-secret-123
spring.cloud.openfeign.circuitbreaker.enabled=true

spring.cloud.openfeign.client.config.aml-client.url=http://192.168.64.1:3001
spring.cloud.openfeign.client.config.aml-client.logger-level=full
spring.cloud.openfeign.client.config.aml-client.default-request-headers.authorization=Bearer xyz123
```

> ⚠️ As URLs, o `x-api-key` e o `Bearer token` acima são valores de exemplo apontando para uma API mockada local. **Substitua `spring.cloud.openfeign.client.config.*.url` pelo endereço do seu servidor de mock** (Mockoon, Postman Mock Server, Insomnia, WireMock, etc.) antes de rodar o projeto.

### Simulando as APIs externas

Como o projeto não inclui as APIs reais de sanções/AML, é necessário mockar dois endpoints:

**Sanction API**
```
GET /sanctions/companies/{registrationNumber}
```
```json
{
  "matches": [
    {
      "entity": "Empresa Exemplo LTDA",
      "list": "OFAC",
      "reason": "Suspeita de fraude",
      "confidenceScore": 0.92
    }
  ]
}
```

**AML API**
```
GET /aml/v1/screening/{registrationNumber}
```
```json
{
  "riskScore": 75,
  "flags": ["HIGH_CASH_VOLUME"],
  "pep": {
    "isPep": true,
    "occurrences": [
      { "personName": "Fulano de Tal", "position": "Secretário Municipal" }
    ]
  }
}
```

## ▶️ Como executar

Pré-requisitos: **JDK 25** e um mock das APIs externas rodando (ex.: Mockoon) no endereço configurado em `application.properties`.

```bash
# clonar o repositório
git clone https://github.com/bartguitar/dio-codigo-bootcamp-java-santander-modulo5-curso4.git
cd dio-codigo-bootcamp-java-santander-modulo5-curso4

# subir a aplicação
./gradlew bootRun
```

A aplicação sobe em `http://localhost:8080`.

## 📡 Endpoints principais

Expostos automaticamente pelo Spring Data REST em `/companies`:

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/companies` | Lista as empresas cadastradas |
| `POST` | `/companies` | Cadastra uma empresa e dispara a análise de risco automaticamente |
| `GET` | `/companies/{id}` | Detalha uma empresa, incluindo `riskAssessment` após a análise |

**Exemplo de criação:**

```bash
curl -X POST http://localhost:8080/companies \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Empresa Exemplo LTDA",
        "registrationNumber": "12345678000199"
      }'
```

Após a criação, consultar `GET /companies/{id}` deve retornar o campo `riskAssessment` preenchido com `score`, `level` e `status`.

## 🧪 Testes

```bash
./gradlew test
```

## 🗺️ Aprendizados do módulo

Este projeto foi construído incrementalmente ao longo do curso, cobrindo:

- Criação de Feign Clients declarativos (`@FeignClient`) e ativação via `@EnableFeignClients`;
- Configuração de URL, headers e nível de log por cliente em `application.properties`;
- Implementação de **fallback** para chamadas externas indisponíveis;
- Ativação de **circuit breaker** (Resilience4j) para chamadas resilientes;
- Consumo de payloads complexos (objetos aninhados) através dos DTOs de resposta;
- Reação a eventos do Spring Data REST (`@HandleAfterCreate`) para orquestrar casos de uso após persistência.

O histórico de commits do repositório segue essa evolução seção a seção (Seção 1 a 7).

---

Feito durante o Bootcamp Santander Back-End Java, em parceria com a [DIO](https://www.dio.me/).
