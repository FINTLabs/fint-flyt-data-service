package no.fintlabs.arkiv.kodeverk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Jacksonized

public class ResourceReference {

    private final String id;
    private final String displayName;

}
