package no.fintlabs.arkiv.sak;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.arkiv.kodeverk.Saksstatus;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.arkiv.kodeverk.SaksstatusResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.arkiv.sak.model.SakDTO;
import no.fintlabs.arkiv.sak.model.SaksansvarligDto;
import no.fintlabs.exceptions.NoSuchLinkException;
import no.fintlabs.kafka.consumer.cache.FintCacheManager;
import no.fintlabs.kafka.consumer.cache.exceptions.NoSuchCacheEntryException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.fintlabs.arkiv.ResourceLinkUtil.getFirstLink;

@Slf4j
@Service
public class SakMapper {

    private final ObjectMapper objectMapper;
    private final FintCacheManager fintCacheManager;

    public SakMapper(ObjectMapper objectMapper, FintCacheManager fintCacheManager) {
        this.objectMapper = objectMapper;
        this.fintCacheManager = fintCacheManager;
    }

    public SakDTO toSakDTO(SakResource sakResource) {
        SakDTO sakDTO = new SakDTO();
        sakDTO.setCaseNumber(sakResource.getMappeId().getIdentifikatorverdi());
        sakDTO.setDate(sakResource.getSaksdato());
        sakDTO.setTitle(sakResource.getTittel());
        sakDTO.setStatus(this.getSaksstatus(sakResource));
        sakDTO.setCaseworker(this.getSaksansvarlig(sakResource));
        return sakDTO;
    }

    private Saksstatus getSaksstatus(SakResource sakResource) {
        String saksstatsaksstatusResourceHrefsHref = this.getSaksstatusResourceHref(sakResource);
        SaksstatusResource saksstatusResource = this.getSaksstatusResource(saksstatsaksstatusResourceHrefsHref);
        return this.objectMapper.convertValue(saksstatusResource, Saksstatus.class);
    }

    private SaksansvarligDto getSaksansvarlig(SakResource sakResource) {
        SaksansvarligDto saksansvarligDto = new SaksansvarligDto();
        try {

            String saksansvarligHref = this.getSaksansvarligHref(sakResource);
            ArkivressursResource arkivressursResource = this.getArkivressursResource(saksansvarligHref);

            Optional.ofNullable(arkivressursResource.getKildesystemId())
                    .map(Identifikator::getIdentifikatorverdi)
                    .ifPresent(saksansvarligDto::setKildesystemId);
            Optional.ofNullable(arkivressursResource.getSystemId())
                    .map(Identifikator::getIdentifikatorverdi)
                    .ifPresent(saksansvarligDto::setSystemId);

            String personalressursResourceHref = this.getPersonalressursResourceHref(arkivressursResource);
            PersonalressursResource personalressursResource = this.getPersonalressursResource(personalressursResourceHref);

            String personResourceHref = this.getPersonResourceHref(personalressursResource);
            PersonResource personResource = this.getPersonResource(personResourceHref);

            Optional.ofNullable(personResource.getNavn())
                    .map(personnavn -> Stream.of(
                                    personnavn.getFornavn(),
                                    personnavn.getMellomnavn(),
                                    personnavn.getEtternavn()
                            ).filter(Objects::nonNull)
                            .collect(Collectors.joining(" ")))
                    .ifPresent(saksansvarligDto::setPersonNavn);

        } catch (NoSuchLinkException | NoSuchCacheEntryException e) {
            log.warn(String.format("Could not completely map saksansvarlig for case with mappeId='%s'", sakResource.getMappeId().getIdentifikatorverdi()), e);
        }
        return saksansvarligDto;
    }

    private String getSaksstatusResourceHref(SakResource sakResource) {
        return getFirstLink(sakResource::getSaksstatus, "SakResource", "saksstatus");
    }

    private String getSaksansvarligHref(SakResource sakResource) {
        return getFirstLink(sakResource::getSaksansvarlig, "SakResource", "saksansvarlig");
    }

    private String getPersonalressursResourceHref(ArkivressursResource arkivressursResource) {
        return getFirstLink(arkivressursResource::getPersonalressurs, "ArkivressursResource", "personalressurs");
    }

    private String getPersonResourceHref(PersonalressursResource personalressursResource) {
        return getFirstLink(personalressursResource::getPerson, "PersonalressursResource", "person");
    }

    private SaksstatusResource getSaksstatusResource(String saksstatusResourceHref) {
        return this.fintCacheManager
                .getCache("arkiv.kodeverk.saksstatus", String.class, SaksstatusResource.class)
                .get(saksstatusResourceHref);
    }

    private ArkivressursResource getArkivressursResource(String arkivressursResourceHref) {
        return this.fintCacheManager
                .getCache("arkiv.noark.arkivressurs", String.class, ArkivressursResource.class)
                .get(arkivressursResourceHref);
    }

    private PersonalressursResource getPersonalressursResource(String personalressursResourceHref) {
        return this.fintCacheManager
                .getCache("administrasjon.personal.personalressurs", String.class, PersonalressursResource.class)
                .get(personalressursResourceHref);
    }

    private PersonResource getPersonResource(String personResourceHref) {
        return this.fintCacheManager
                .getCache("administrasjon.personal.person", String.class, PersonResource.class)
                .get(personResourceHref);
    }

}
