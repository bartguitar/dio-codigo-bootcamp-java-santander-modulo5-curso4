package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.infrastructure.rest.dto;

import br.com.dio.dioprojetomodulo5curso4springcloudopenfeign.domain.ComplianceScreening;

import java.util.List;

public record SanctionResult(List<SanctionMatch> matches) {

    public List<ComplianceScreening.SanctionIdentity> toDomain() {
        if (matches() == null) {
            return List.of();
        }


        return matches().stream()
                .map(match -> new ComplianceScreening.SanctionIdentity(
                        match.entity(),
                        match.list(),
                        match.reason(),
                        match.confidenceScore() != null ? match.confidenceScore() : 0.0
                ))
                .toList();
    }

    public record SanctionMatch(
            String entity,
            String list,
            String reason,
            Double confidenceScore
    ) {
    }

}