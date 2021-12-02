package no.fintlabs.arkiv.sak;

import lombok.Data;

import java.util.Date;

@Data
public class SakDTO {

    private String caseNumber;
    private Date Date;
    private String Status;
    private String Title;
    private String Caseworker;

}
