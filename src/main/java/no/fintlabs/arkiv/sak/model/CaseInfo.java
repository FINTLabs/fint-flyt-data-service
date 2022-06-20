package no.fintlabs.arkiv.sak.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CaseInfo {
    private final String caseId;
    private final String sourceApplicationInstanceId;
    private final CaseManager caseManager;
    private final AdministrativeUnit administrativeUnit;
    private final CaseStatus status;
}
