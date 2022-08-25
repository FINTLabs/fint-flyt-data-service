package no.fintlabs.arkiv.kodeverk;

import no.fint.model.resource.FintLinks;
import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.links.ResourceLinkUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static no.fintlabs.resourceserver.UrlPaths.INTERNAL_API;

@RestController
@RequestMapping(INTERNAL_API + "/kodeverk")
public class CodelistController {

    private final FintCache<String, AdministrativEnhetResource> administrativEnhetResourceCache;
    private final FintCache<String, ArkivdelResource> arkivdelResourceCache;
    private final FintCache<String, ArkivressursResource> arkivressursResourceCache;
    private final FintCache<String, DokumentStatusResource> dokumentStatusResourceCache;
    private final FintCache<String, DokumentTypeResource> dokumentTypeResourceCache;
    private final FintCache<String, KlassifikasjonssystemResource> klassifikasjonssystemResourceCache;
    private final FintCache<String, SaksstatusResource> saksstatusResourceCache;
    private final FintCache<String, SkjermingshjemmelResource> skjermingshjemmelResourceCache;
    private final FintCache<String, TilgangsrestriksjonResource> tilgangsrestriksjonResourceCache;
    private final FintCache<String, JournalStatusResource> journalStatusResourceCache;
    private final FintCache<String, VariantformatResource> variantformatResourceCache;

    private final ArkivressursReferenceMapper arkivressursReferenceMapper;

    public CodelistController(
            FintCache<String, AdministrativEnhetResource> administrativEnhetResourceCache,
            FintCache<String, ArkivdelResource> arkivdelResourceCache,
            FintCache<String, ArkivressursResource> arkivressursResourceCache,
            FintCache<String, DokumentStatusResource> dokumentStatusResourceCache,
            FintCache<String, DokumentTypeResource> dokumentTypeResourceCache,
            FintCache<String, KlassifikasjonssystemResource> klassifikasjonssystemResourceCache,
            FintCache<String, SaksstatusResource> saksstatusResourceCache,
            FintCache<String, SkjermingshjemmelResource> skjermingshjemmelResourceCache,
            FintCache<String, TilgangsrestriksjonResource> tilgangsrestriksjonResourceCache,
            FintCache<String, JournalStatusResource> journalStatusResourceCache,
            FintCache<String, VariantformatResource> variantformatResourceCache, ArkivressursReferenceMapper arkivressursReferenceMapper) {
        this.administrativEnhetResourceCache = administrativEnhetResourceCache;
        this.arkivdelResourceCache = arkivdelResourceCache;
        this.arkivressursResourceCache = arkivressursResourceCache;
        this.dokumentStatusResourceCache = dokumentStatusResourceCache;
        this.dokumentTypeResourceCache = dokumentTypeResourceCache;
        this.klassifikasjonssystemResourceCache = klassifikasjonssystemResourceCache;
        this.saksstatusResourceCache = saksstatusResourceCache;
        this.skjermingshjemmelResourceCache = skjermingshjemmelResourceCache;
        this.tilgangsrestriksjonResourceCache = tilgangsrestriksjonResourceCache;
        this.journalStatusResourceCache = journalStatusResourceCache;
        this.variantformatResourceCache = variantformatResourceCache;
        this.arkivressursReferenceMapper = arkivressursReferenceMapper;
    }

    @GetMapping("administrativenhet")
    public ResponseEntity<Collection<ResourceReference>> getAdministrativEnheter() {
        return ResponseEntity.ok(
                administrativEnhetResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(administrativEnhetResource -> this.mapToResourceReference(administrativEnhetResource, administrativEnhetResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("klassifikasjonssystem")
    public ResponseEntity<Collection<ResourceReference>> getKlassifikasjonssystem() {
        return ResponseEntity.ok(
                klassifikasjonssystemResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(klassifikasjonssystemResource -> this.mapToResourceReference(klassifikasjonssystemResource, klassifikasjonssystemResource.getTittel()))
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
                        .map(klasse -> new ResourceReference(klasse.getKlasseId(), klasse.getTittel()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("sakstatus")
    public ResponseEntity<Collection<ResourceReference>> getSakstatus() {
        return ResponseEntity.ok(
                saksstatusResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(saksstatusResource -> this.mapToResourceReference(saksstatusResource, saksstatusResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("arkivdel")
    public ResponseEntity<Collection<ResourceReference>> getArkivdel() {
        return ResponseEntity.ok(
                arkivdelResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(arkivdelResource -> this.mapToResourceReference(arkivdelResource, arkivdelResource.getTittel()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("skjermingshjemmel")
    public ResponseEntity<Collection<ResourceReference>> getSkjermingshjemmel() {
        return ResponseEntity.ok(
                skjermingshjemmelResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(skjermingshjemmelResource -> this.mapToResourceReference(
                                skjermingshjemmelResource,
                                String.format("%s (%s)", skjermingshjemmelResource.getNavn(), skjermingshjemmelResource.getSystemId())
                        ))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("tilgangsrestriksjon")
    public ResponseEntity<Collection<ResourceReference>> getTilgangsrestriksjon() {
        return ResponseEntity.ok(
                tilgangsrestriksjonResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(tilgangsrestriksjonResource -> this.mapToResourceReference(tilgangsrestriksjonResource, tilgangsrestriksjonResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("dokumentstatus")
    public ResponseEntity<Collection<ResourceReference>> getDokumentstatus() {
        return ResponseEntity.ok(
                dokumentStatusResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(dokumentStatusResource -> this.mapToResourceReference(dokumentStatusResource, dokumentStatusResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("dokumenttype")
    public ResponseEntity<Collection<ResourceReference>> getDokumenttype() {
        return ResponseEntity.ok(
                dokumentTypeResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(dokumentTypeResource -> this.mapToResourceReference(dokumentTypeResource, dokumentTypeResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("journalstatus")
    public ResponseEntity<Collection<ResourceReference>> getJournalstatus() {
        return ResponseEntity.ok(
                journalStatusResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(journalStatusResource -> this.mapToResourceReference(journalStatusResource, journalStatusResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("variantformat")
    public ResponseEntity<Collection<ResourceReference>> getVariantformat() {
        return ResponseEntity.ok(
                variantformatResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(variantformatResource -> this.mapToResourceReference(variantformatResource, variantformatResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("arkivressurs")
    public ResponseEntity<Collection<ResourceReference>> getArkivressurs() {
        return ResponseEntity.ok(
                arkivressursResourceCache
                        .getAllDistinct()
                        .stream()
                        .map(this.arkivressursReferenceMapper::map)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
        );
    }

    private ResourceReference mapToResourceReference(FintLinks resource, String displayName) {
        return new ResourceReference(ResourceLinkUtil.getFirstSelfLink(resource), displayName);
    }

}
