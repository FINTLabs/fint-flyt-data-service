package no.fintlabs.arkiv.sak;

import lombok.Data;
import no.fint.model.arkiv.kodeverk.Saksstatus;
import no.fint.model.arkiv.noark.Arkivressurs;

import java.util.Date;

@Data
public class SakDTO {

    private String caseNumber;
    private Date Date;
    private Saksstatus Status;
    private String Title;
    private Arkivressurs Caseworker;

}
