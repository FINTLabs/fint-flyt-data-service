package no.fintlabs.arkiv.sak

import com.fasterxml.jackson.databind.ObjectMapper
import no.fint.model.arkiv.kodeverk.Saksstatus
import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.felles.kompleksedatatyper.Personnavn
import no.fint.model.resource.Link
import no.fint.model.resource.administrasjon.personal.PersonalressursResource
import no.fint.model.resource.arkiv.kodeverk.SaksstatusResource
import no.fint.model.resource.arkiv.noark.ArkivressursResource
import no.fint.model.resource.arkiv.noark.SakResource
import no.fint.model.resource.felles.PersonResource
import no.fintlabs.arkiv.sak.model.SakDTO
import no.fintlabs.arkiv.sak.model.SaksansvarligDto
import no.fintlabs.cache.FintCache
import no.fintlabs.cache.FintCacheManager
import spock.lang.Specification

import static java.util.Arrays.asList

class SakMapperSpec extends Specification {

    SakMapper sakMapper
    Saksstatus saksstatus

    void setup() {
        sakMapper = createSakMapper()
    }

    private SakMapper createSakMapper() {
        ObjectMapper objectMapper = Stub(ObjectMapper.class)
        FintCacheManager cacheManager = Stub(FintCacheManager.class)

        FintCache<String, SaksstatusResource> saksstatusCache = Stub(FintCache.class)
        SaksstatusResource saksstatusResource = Mock(SaksstatusResource.class)
        saksstatusCache.get("testSaksstatusHref") >> saksstatusResource
        cacheManager.getCache("arkiv.kodeverk.saksstatus", String.class, SaksstatusResource.class) >> saksstatusCache

        saksstatus = Mock(Saksstatus.class)
        objectMapper.convertValue(saksstatusResource, Saksstatus.class) >> saksstatus

        FintCache<String, ArkivressursResource> arkivressursCache = Stub(FintCache.class)
        arkivressursCache.get("testarkivressursHref") >> createArkivressursResource()
        cacheManager.getCache("arkiv.noark.arkivressurs", String.class, ArkivressursResource.class) >> arkivressursCache

        FintCache<String, PersonalressursResource> personalressursCache = Stub(FintCache.class)
        personalressursCache.get("testPersonalressursHref") >> createPersonalressursResource()
        cacheManager.getCache("administrasjon.personal.personalressurs", String.class, PersonalressursResource.class) >> personalressursCache

        FintCache<String, PersonResource> personCache = Stub(FintCache.class)
        personCache.get("testPersonHref") >> createPersonResource()
        cacheManager.getCache("administrasjon.personal.person", String.class, PersonResource.class) >> personCache

        return new SakMapper(objectMapper, cacheManager)
    }

    private Identifikator createIdentifikator(String value) {
        Identifikator identifikator = new Identifikator()
        identifikator.setIdentifikatorverdi(value)
        return identifikator
    }

    private ArkivressursResource createArkivressursResource() {
        ArkivressursResource arkivressursResource = Stub(ArkivressursResource.class)
        arkivressursResource.getSystemId() >> createIdentifikator("testArkivressursSystemId")
        arkivressursResource.getKildesystemId() >> createIdentifikator("testArkivressursKildesystemId")
        arkivressursResource.getPersonalressurs() >> asList(new Link("testPersonalressursHref"))
        return arkivressursResource
    }

    private PersonalressursResource createPersonalressursResource() {
        PersonalressursResource personalressursResource = Stub(PersonalressursResource.class)
        personalressursResource.getPerson() >> asList(new Link("testPersonHref"))
        return personalressursResource
    }

    private PersonResource createPersonResource() {
        PersonResource personResource = Stub(PersonResource.class)
        Personnavn personnavn = new Personnavn()
        personnavn.setFornavn("testFornavn")
        personnavn.setEtternavn("testEtternavn")
        personResource.getNavn() >> personnavn
        return personResource
    }

    def 'should map to DTO if all resource and cache values exist'() {
        given:
        SakResource sakResource = Stub(SakResource.class)
        sakResource.getMappeId() >> createIdentifikator("testMappeId")
        sakResource.getSaksdato() >> new Date(2021, 1, 1)
        sakResource.getTittel() >> "testTittel"
        sakResource.getSaksstatus() >> asList(new Link("testSaksstatusHref"))
        sakResource.getSaksansvarlig() >> asList(new Link("testarkivressursHref"))

        when:
        SakDTO sakDTO = sakMapper.toSakDTO(sakResource)

        then:
        Objects.nonNull(sakDTO)
        sakDTO.getCaseNumber() == "testMappeId"
        sakDTO.getDate() == new Date(2021, 1, 1)
        sakDTO.getStatus() == saksstatus
        sakDTO.getTitle() == "testTittel"

        SaksansvarligDto saksansvarligDto = sakDTO.getCaseworker()
        Objects.nonNull(saksansvarligDto)
        saksansvarligDto.getKildesystemId() == "testArkivressursKildesystemId"
        saksansvarligDto.getSystemId() == "testArkivressursSystemId"
        saksansvarligDto.getPersonNavn() == "testFornavn testEtternavn"
    }

}
