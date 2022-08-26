package no.fintlabs.arkiv.sak.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArchiveCaseIdRequestParams {
    private final String sourceApplicationId;
    private final String sourceApplicationInstanceId;
}
