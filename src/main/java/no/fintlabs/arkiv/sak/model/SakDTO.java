package no.fintlabs.arkiv.sak.model;

import lombok.Data;
import no.fint.model.arkiv.kodeverk.Saksstatus;

import java.util.Date;

@Data
public class SakDTO {

    private String caseNumber;
    private Date Date;
    private Saksstatus Status;
    private String Title;
    private SaksansvarligDto Caseworker;

}
