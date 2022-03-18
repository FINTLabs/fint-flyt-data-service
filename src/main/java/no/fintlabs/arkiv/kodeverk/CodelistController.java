package no.fintlabs.arkiv.kodeverk;

import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fintlabs.cache.FintCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/kodeverk")
public class CodelistController {

    private final FintCacheManager fintCacheManager;

    public CodelistController(FintCacheManager fintCacheManager) {
        this.fintCacheManager = fintCacheManager;
    }

    @GetMapping("administrativenhet")
    public ResponseEntity<Collection<AdministrativEnhetResource>> getAdministrativEnheter() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.noark.administrativenhet", String.class, AdministrativEnhetResource.class)
                        .getAll()
        );
    }

    @GetMapping("klassifikasjonssystem")
    public ResponseEntity<Collection<KlassifikasjonssystemResource>> getKlassifikasjonssystem() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.noark.klassifikasjonssystem", String.class, KlassifikasjonssystemResource.class)
                        .getAll()
        );
    }

    @GetMapping("rolle")
    public ResponseEntity<Collection<RolleResource>> getRolle() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.noark.rolle", String.class, RolleResource.class)
                        .getAll()
        );
    }

    @GetMapping("sakstatus")
    public ResponseEntity<Collection<SaksstatusResource>> getSakstatus() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.saksstatus", String.class, SaksstatusResource.class)
                        .getAll()
        );
    }

    @GetMapping("arkivdel")
    public ResponseEntity<Collection<ArkivdelResource>> getArkivdel() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.noark.arkivdel", String.class, ArkivdelResource.class)
                        .getAll()
        );
    }

    @GetMapping("skjermingshjemmel")
    public ResponseEntity<Collection<SkjermingshjemmelResource>> getSkjermingshjemmel() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.skjermingshjemmel", String.class, SkjermingshjemmelResource.class)
                        .getAll()
        );
    }

    @GetMapping("tilgangsrestriksjon")
    public ResponseEntity<Collection<TilgangsrestriksjonResource>> getTilgangsrestriksjon() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.tilgangsrestriksjon", String.class, TilgangsrestriksjonResource.class)
                        .getAll()
        );
    }

    @GetMapping("klassifikasjonstype")
    public ResponseEntity<Collection<KlassifikasjonstypeResource>> getKlassifikasjonstype() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.klassifikasjonstype", String.class, KlassifikasjonstypeResource.class)
                        .getAll()
        );
    }

    @GetMapping("dokumentstatus")
    public ResponseEntity<Collection<DokumentStatusResource>> getDokumentstatus() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.dokumentstatus", String.class, DokumentStatusResource.class)
                        .getAll()
        );
    }

    @GetMapping("dokumenttype")
    public ResponseEntity<Collection<DokumentTypeResource>> getDokumenttype() {
        return ResponseEntity.ok(
                fintCacheManager
                        .getCache("arkiv.kodeverk.dokumenttype", String.class, DokumentTypeResource.class)
                        .getAll()
        );
    }
}
