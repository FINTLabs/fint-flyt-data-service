package no.fintlabs.arkiv.sak.model;

import lombok.Data;
import no.fint.model.arkiv.kodeverk.Saksstatus;

import java.util.Date;

@Data
public class Case {

    private String caseNumber;
    private Date date;
    private Saksstatus status;
    private String title;
    private CaseWorker caseworker;

}
