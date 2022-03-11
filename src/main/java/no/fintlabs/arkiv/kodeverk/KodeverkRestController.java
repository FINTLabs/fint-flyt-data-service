package no.fintlabs.arkiv.kodeverk;

import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fintlabs.cache.FintCacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("kodeverk/")
public class KodeverkRestController {

    private final FintCacheManager fintCacheManager;

    public KodeverkRestController(FintCacheManager fintCacheManager) {
        this.fintCacheManager = fintCacheManager;
    }

    @GetMapping("administrativenhet")
    public Collection<AdministrativEnhetResource> getAdministrativEnheter() {
        return fintCacheManager.getCache("arkiv.noark.administrativenhet", String.class, AdministrativEnhetResource.class).getAll();
    }

    @GetMapping("klassifikasjonssystem")
    public Collection<KlassifikasjonssystemResource> getKlassifikasjonssystem() {
        return fintCacheManager.getCache("arkiv.noark.klassifikasjonssystem", String.class, KlassifikasjonssystemResource.class).getAll();
    }

    @GetMapping("rolle")
    public Collection<RolleResource> getRolle() {
        return fintCacheManager.getCache("arkiv.noark.rolle", String.class, RolleResource.class).getAll();
    }

    @GetMapping("sakstatus")
    public Collection<SaksstatusResource> getSakstatus() {
        return fintCacheManager.getCache("arkiv.kodeverk.saksstatus", String.class, SaksstatusResource.class).getAll();
    }

    @GetMapping("arkivdel")
    public Collection<ArkivdelResource> getArkivdel() {
        return fintCacheManager.getCache("arkiv.noark.arkivdel", String.class, ArkivdelResource.class).getAll();
    }

    @GetMapping("skjermingshjemmel")
    public Collection<SkjermingshjemmelResource> getSkjermingshjemmel() {
        return fintCacheManager.getCache("arkiv.kodeverk.skjermingshjemmel", String.class, SkjermingshjemmelResource.class).getAll();
    }

    @GetMapping("tilgangsrestriksjon")
    public Collection<TilgangsrestriksjonResource> getTilgangsrestriksjon() {
        return fintCacheManager.getCache("arkiv.kodeverk.tilgangsrestriksjon", String.class, TilgangsrestriksjonResource.class).getAll();
    }

    @GetMapping("klassifikasjonstype")
    public Collection<KlassifikasjonstypeResource> getKlassifikasjonstype() {
        return fintCacheManager.getCache("arkiv.kodeverk.klassifikasjonstype", String.class, KlassifikasjonstypeResource.class).getAll();
    }

    @GetMapping("dokumentstatus")
    public Collection<DokumentStatusResource> getDokumentstatus() {
        return fintCacheManager.getCache("arkiv.kodeverk.dokumentstatus", String.class, DokumentStatusResource.class).getAll();
    }

    @GetMapping("dokumenttype")
    public Collection<DokumentTypeResource> getDokumenttype() {
        return fintCacheManager.getCache("arkiv.kodeverk.dokumenttype", String.class, DokumentTypeResource.class).getAll();
    }
}
