package no.fintlabs.arkiv.sak;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder

public class CaseTitle {

    private final String value;

    @Override
    public String toString() {
        return "Sensitive data omitted";
    }
}
