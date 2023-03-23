package no.fintlabs.arkiv.kodeverk;

import no.fint.model.felles.basisklasser.Begrep;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.links.ResourceLinkUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static no.fintlabs.resourceserver.UrlPaths.INTERNAL_API;

@RestController
@RequestMapping(INTERNAL_API + "/arkiv/kodeverk")
public class CodelistController {

    private final FintCache<String, AdministrativEnhetResource> administrativEnhetResourceCache;
    private final FintCache<String, ArkivdelResource> arkivdelResourceCache;
    private final FintCache<String, ArkivressursResource> arkivressursResourceCache;
    private final FintCache<String, DokumentStatusResource> dokumentStatusResourceCache;
    private final FintCache<String, DokumentTypeResource> dokumentTypeResourceCache;
    private final FintCache<String, KlassifikasjonssystemResource> klassifikasjonssystemResourceCache;
    private final FintCache<String, PartRolleResource> partRolleResourceCache;
    private final FintCache<String, KorrespondansepartTypeResource> korrespondansepartTypeResourceCache;
    private final FintCache<String, SaksstatusResource> saksstatusResourceCache;
    private final FintCache<String, SkjermingshjemmelResource> skjermingshjemmelResourceCache;
    private final FintCache<String, TilgangsrestriksjonResource> tilgangsrestriksjonResourceCache;
    private final FintCache<String, JournalStatusResource> journalStatusResourceCache;
    private final FintCache<String, JournalpostTypeResource> journalpostTypeResourceCache;
    private final FintCache<String, SaksmappetypeResource> saksmappetypeResourceCache;
    private final FintCache<String, VariantformatResource> variantformatResourceCache;
    private final FintCache<String, FormatResource> formatResourceCache;

    private final ArkivressursDisplayNameMapper arkivressursDisplayNameMapper;

    public CodelistController(
            FintCache<String, AdministrativEnhetResource> administrativEnhetResourceCache,
            FintCache<String, ArkivdelResource> arkivdelResourceCache,
            FintCache<String, ArkivressursResource> arkivressursResourceCache,
            FintCache<String, DokumentStatusResource> dokumentStatusResourceCache,
            FintCache<String, DokumentTypeResource> dokumentTypeResourceCache,
            FintCache<String, KlassifikasjonssystemResource> klassifikasjonssystemResourceCache,
            FintCache<String, PartRolleResource> partRolleResourceCache,
            FintCache<String, KorrespondansepartTypeResource> korrespondansepartTypeResourceCache,
            FintCache<String, SaksstatusResource> saksstatusResourceCache,
            FintCache<String, SkjermingshjemmelResource> skjermingshjemmelResourceCache,
            FintCache<String, TilgangsrestriksjonResource> tilgangsrestriksjonResourceCache,
            FintCache<String, JournalStatusResource> journalStatusResourceCache,
            FintCache<String, JournalpostTypeResource> journalpostTypeResourceCache,
            FintCache<String, SaksmappetypeResource> saksmappetypeResourceCache,
            FintCache<String, VariantformatResource> variantformatResourceCache,
            FintCache<String, FormatResource> formatResourceCache,
            ArkivressursDisplayNameMapper arkivressursDisplayNameMapper
    ) {
        this.administrativEnhetResourceCache = administrativEnhetResourceCache;
        this.arkivdelResourceCache = arkivdelResourceCache;
        this.arkivressursResourceCache = arkivressursResourceCache;
        this.dokumentStatusResourceCache = dokumentStatusResourceCache;
        this.dokumentTypeResourceCache = dokumentTypeResourceCache;
        this.klassifikasjonssystemResourceCache = klassifikasjonssystemResourceCache;
        this.partRolleResourceCache = partRolleResourceCache;
        this.korrespondansepartTypeResourceCache = korrespondansepartTypeResourceCache;
        this.saksstatusResourceCache = saksstatusResourceCache;
        this.skjermingshjemmelResourceCache = skjermingshjemmelResourceCache;
        this.tilgangsrestriksjonResourceCache = tilgangsrestriksjonResourceCache;
        this.journalStatusResourceCache = journalStatusResourceCache;
        this.journalpostTypeResourceCache = journalpostTypeResourceCache;
        this.saksmappetypeResourceCache = saksmappetypeResourceCache;
        this.variantformatResourceCache = variantformatResourceCache;
        this.formatResourceCache = formatResourceCache;
        this.arkivressursDisplayNameMapper = arkivressursDisplayNameMapper;
    }

    @GetMapping("administrativenhet")
    public ResponseEntity<Collection<ResourceReference>> getAdministrativEnheter() {
        return ResponseEntity.ok(
                administrativEnhetResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(administrativEnhetResource -> this.mapToResourceReference(
                                administrativEnhetResource,
                                administrativEnhetResource.getSystemId(),
                                administrativEnhetResource.getNavn()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("klassifikasjonssystem")
    public ResponseEntity<Collection<ResourceReference>> getKlassifikasjonssystem() {
        return ResponseEntity.ok(
                klassifikasjonssystemResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(klassifikasjonssystemResource -> this.mapToResourceReference(
                                klassifikasjonssystemResource,
                                klassifikasjonssystemResource.getSystemId(),
                                klassifikasjonssystemResource.getTittel()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("klasse")
    public ResponseEntity<Collection<ResourceReference>> getKlasse(@RequestParam String klassifikasjonssystemLink) {
        return ResponseEntity.ok(
                klassifikasjonssystemResourceCache
                        .get(klassifikasjonssystemLink)
                        .getKlasse()
                        .stream()
                        .map(klasse -> this.mapToResourceReference(klasse.getKlasseId(), klasse.getKlasseId(), klasse.getTittel()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("partrolle")
    public ResponseEntity<Collection<ResourceReference>> getPartRolle() {
        return ResponseEntity.ok(
                partRolleResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(partRolleResource -> this.mapToResourceReference(
                                partRolleResource,
                                partRolleResource.getSystemId(),
                                partRolleResource.getNavn()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("korrespondanseparttype")
    public ResponseEntity<Collection<ResourceReference>> getKorrespondansepartType() {
        return ResponseEntity.ok(
                korrespondansepartTypeResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(korrespondansepartTypeResource -> this.mapToResourceReference(
                                korrespondansepartTypeResource,
                                korrespondansepartTypeResource.getSystemId(),
                                korrespondansepartTypeResource.getNavn()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("arkivdel")
    public ResponseEntity<Collection<ResourceReference>> getArkivdel() {
        return ResponseEntity.ok(
                arkivdelResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(arkivdelResource -> this.mapToResourceReference(
                                arkivdelResource,
                                arkivdelResource.getSystemId(),
                                arkivdelResource.getTittel()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("arkivressurs")
    public ResponseEntity<Collection<ResourceReference>> getArkivressurs() {
        return ResponseEntity.ok(
                arkivressursResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(arkivressurs -> arkivressursDisplayNameMapper.getDisplayName(arkivressurs)
                                .map(displayName -> this.mapToResourceReference(
                                        arkivressurs,
                                        arkivressurs.getSystemId(),
                                        displayName
                                )))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("sakstatus")
    public ResponseEntity<Collection<ResourceReference>> getSakstatus() {
        return getBegrepResourceReferences(saksstatusResourceCache);
    }

    @GetMapping("skjermingshjemmel")
    public ResponseEntity<Collection<ResourceReference>> getSkjermingshjemmel() {
        return getBegrepResourceReferences(skjermingshjemmelResourceCache);
    }

    @GetMapping("tilgangsrestriksjon")
    public ResponseEntity<Collection<ResourceReference>> getTilgangsrestriksjon() {
        return getBegrepResourceReferences(tilgangsrestriksjonResourceCache);
    }

    @GetMapping("dokumentstatus")
    public ResponseEntity<Collection<ResourceReference>> getDokumentstatus() {
        return getBegrepResourceReferences(dokumentStatusResourceCache);
    }

    @GetMapping("dokumenttype")
    public ResponseEntity<Collection<ResourceReference>> getDokumenttype() {
        return getBegrepResourceReferences(dokumentTypeResourceCache);
    }

    @GetMapping("journalstatus")
    public ResponseEntity<Collection<ResourceReference>> getJournalstatus() {
        return getBegrepResourceReferences(journalStatusResourceCache);
    }

    @GetMapping("journalposttype")
    public ResponseEntity<Collection<ResourceReference>> getJournalposttype() {
        return getBegrepResourceReferences(journalpostTypeResourceCache);
    }

    @GetMapping("saksmappetype")
    public ResponseEntity<Collection<ResourceReference>> getSaksmappetype() {
        return getBegrepResourceReferences(saksmappetypeResourceCache);
    }

    @GetMapping("variantformat")
    public ResponseEntity<Collection<ResourceReference>> getVariantformat() {
        return getBegrepResourceReferences(variantformatResourceCache);
    }

    @GetMapping("format")
    public ResponseEntity<Collection<ResourceReference>> getFormat() {
        return getBegrepResourceReferences(formatResourceCache);
    }

    private <R extends Begrep & FintLinks> ResponseEntity<Collection<ResourceReference>> getBegrepResourceReferences(FintCache<String, R> resouceCache) {
        return ResponseEntity.ok(
                resouceCache
                        .getAllDistinct()
                        .stream()
                        .map(this::mapToResourceReference)
                        .collect(Collectors.toList())
        );
    }

    private <R extends Begrep & FintLinks> ResourceReference mapToResourceReference(R resource) {
        return mapToResourceReference(ResourceLinkUtil.getFirstSelfLink(resource), resource.getSystemId().getIdentifikatorverdi(), resource.getNavn());
    }

    private ResourceReference mapToResourceReference(FintLinks resource, Identifikator id, String displayName) {
        return mapToResourceReference(ResourceLinkUtil.getFirstSelfLink(resource), id.getIdentifikatorverdi(), displayName);
    }

    private ResourceReference mapToResourceReference(String id, String displayId, String displayName) {
        return new ResourceReference(
                id,
                String.format("[%s] %s", displayId, displayName)
        );
    }

}
