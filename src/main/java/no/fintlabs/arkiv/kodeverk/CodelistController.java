package no.fintlabs.arkiv.kodeverk;

import no.fint.model.resource.FintLinks;
import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fintlabs.cache.FintCacheManager;
import no.fintlabs.links.ResourceLinkUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/kodeverk")
public class CodelistController {

    private final FintCacheManager fintCacheManager;
    private final ArkivressursReferenceMapper arkivressursReferenceMapper;

    public CodelistController(FintCacheManager fintCacheManager, ArkivressursReferenceMapper arkivressursReferenceMapper) {
        this.fintCacheManager = fintCacheManager;
        this.arkivressursReferenceMapper = arkivressursReferenceMapper;
    }

    @GetMapping("administrativenhet")
    public ResponseEntity<Collection<ResourceReference>> getAdministrativEnheter() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.noark.administrativenhet", String.class, AdministrativEnhetResource.class)
                        .getAllDistinct()
                        .stream()
                        .map(administrativEnhetResource -> this.mapToResourceReference(administrativEnhetResource, administrativEnhetResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("klassifikasjonssystem")
    public ResponseEntity<Collection<ResourceReference>> getKlassifikasjonssystem() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.noark.klassifikasjonssystem", String.class, KlassifikasjonssystemResource.class)
                        .getAllDistinct()
                        .stream()
                        .map(klassifikasjonssystemResource -> this.mapToResourceReference(klassifikasjonssystemResource, klassifikasjonssystemResource.getTittel()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("klasse/{klassifikasjonssystemLink}")
    public ResponseEntity<Collection<ResourceReference>> getKlasse(@PathVariable String klassifikasjonssystemLink) {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.noark.klassifikasjonssystem", String.class, KlassifikasjonssystemResource.class)
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
                fintCacheManager
                        .getCache("arkiv.kodeverk.saksstatus", String.class, SaksstatusResource.class)
                        .getAllDistinct()
                        .stream()
                        .map(saksstatusResource -> this.mapToResourceReference(saksstatusResource, saksstatusResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("arkivdel")
    public ResponseEntity<Collection<ResourceReference>> getArkivdel() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.noark.arkivdel", String.class, ArkivdelResource.class)
                        .getAllDistinct()
                        .stream()
                        .map(arkivdelResource -> this.mapToResourceReference(arkivdelResource, arkivdelResource.getTittel()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("skjermingshjemmel")
    public ResponseEntity<Collection<ResourceReference>> getSkjermingshjemmel() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.skjermingshjemmel", String.class, SkjermingshjemmelResource.class)
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
                fintCacheManager
                        .getCache("arkiv.kodeverk.tilgangsrestriksjon", String.class, TilgangsrestriksjonResource.class)
                        .getAllDistinct()
                        .stream()
                        .map(tilgangsrestriksjonResource -> this.mapToResourceReference(tilgangsrestriksjonResource, tilgangsrestriksjonResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("dokumentstatus")
    public ResponseEntity<Collection<ResourceReference>> getDokumentstatus() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.dokumentstatus", String.class, DokumentStatusResource.class)
                        .getAllDistinct()
                        .stream()
                        .map(dokumentStatusResource -> this.mapToResourceReference(dokumentStatusResource, dokumentStatusResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("dokumenttype")
    public ResponseEntity<Collection<ResourceReference>> getDokumenttype() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.dokumenttype", String.class, DokumentTypeResource.class)
                        .getAllDistinct()
                        .stream()
                        .map(dokumentTypeResource -> this.mapToResourceReference(dokumentTypeResource, dokumentTypeResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("journalstatus")
    public ResponseEntity<Collection<ResourceReference>> getJournalstatus() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.journalstatus", String.class, JournalStatusResource.class)
                        .getAllDistinct()
                        .stream()
                        .map(journalStatusResource -> this.mapToResourceReference(journalStatusResource, journalStatusResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("variantformat")
    public ResponseEntity<Collection<ResourceReference>> getVariantformat() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.variantformat", String.class, VariantformatResource.class)
                        .getAllDistinct()
                        .stream()
                        .map(variantformatResource -> this.mapToResourceReference(variantformatResource, variantformatResource.getNavn()))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("arkivressurs")
    public ResponseEntity<Collection<ResourceReference>> getArkivressurs() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.noark.arkivressurs", String.class, ArkivressursResource.class)
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
