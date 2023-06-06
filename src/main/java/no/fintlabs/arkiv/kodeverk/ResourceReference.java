package no.fintlabs.arkiv.kodeverk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ResourceReference {

    private final String id;
    private final String displayName;

}
