package no.fintlabs.arkiv.sak;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.arkiv.kodeverk.Saksstatus;
import no.fint.model.resource.arkiv.kodeverk.SaksstatusResource;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.arkiv.kodeverk.consumers.SaksstatusConsumer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SakMapper {

    private final SaksstatusConsumer saksstatusConsumer;
    private final ObjectMapper objectMapper;

    private SakMapper(SaksstatusConsumer saksstatusConsumer, ObjectMapper objectMapper) {
        this.saksstatusConsumer = saksstatusConsumer;
        this.objectMapper = objectMapper;
    }

    public SakDTO toSakDTO(SakResource sakResource) {
        SakDTO sakDTO = new SakDTO();

        sakDTO.setCaseNumber(sakResource.getMappeId().getIdentifikatorverdi());
        sakDTO.setDate(sakResource.getSaksdato());
        sakDTO.setTitle(sakResource.getTittel());

        if (!sakResource.getSaksstatus().isEmpty()) {
            Optional<SaksstatusResource> saksstatusResource = this.saksstatusConsumer.getResourceCache()
                    .getBySelfLink(sakResource.getSaksstatus().get(0).getHref());
            saksstatusResource.ifPresent(resource -> {
                Saksstatus saksstatus = this.objectMapper.convertValue(resource, Saksstatus.class);
                sakDTO.setStatus(saksstatus);
            });
        }



        // Arkivressurs  ??:
        //sakDTO.setCaseworker = sakResource.getSaksansvarlig()

        return sakDTO;
    }

}
