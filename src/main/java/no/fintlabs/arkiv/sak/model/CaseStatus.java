package no.fintlabs.arkiv.sak.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CaseStatus {
    private final String name;
    private final String code;
}
