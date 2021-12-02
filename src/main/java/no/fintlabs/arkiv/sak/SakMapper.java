package no.fintlabs.arkiv.sak;

import no.fint.model.resource.arkiv.noark.SakResource;

public class SakMapper {

    //private SaksstatusConsumer

    private SakMapper() {
    }

    public static SakDTO toSakDTO(SakResource sakResource) {
        SakDTO sakDTO = new SakDTO();

        sakDTO.setCaseNumber(sakResource.getMappeId().getIdentifikatorverdi());
        sakDTO.setDate(sakResource.getSaksdato());
        sakDTO.setTitle(sakResource.getTittel());

        //sakResource.getSaksstatus()

        //Status;
        //sakDTO.setStatus = sakResource.getSaksstatus()

        // Arkivressurs  ??:
        //sakDTO.setCaseworker = sakResource.getSaksansvarlig()

        return sakDTO;
    }
}
