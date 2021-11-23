package no.fintlabs.arkiv.kodeverk;

import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("kodeverk/")
public class KodeverkRestController {

    private final AdministrativEnhetEntityConsumer administrativEnhetEntityConsumer;
    private final KlassifikasjonssystemConsumer klassifikasjonssystemConsumer;
    private final RolleConsumer rolleConsumer;
    private final SakstatusConsumer sakstatusConsumer;
    private final ArkivdelConsumer arkivdelConsumer;
    private final SkjermingshjemmelConsumer skjermingshjemmelConsumer;
    private final TilgangsrestriksjonConsumer tilgangsrestriksjonConsumer;
    private final KlassifikasjonstypeConsumer klassifikasjonstypeConsumer;
    private final DokumentstatusConsumer dokumentstatusConsumer;
    private final DokumenttypeConsumer dokumenttypeConsumer;

    public KodeverkRestController(AdministrativEnhetEntityConsumer administrativEnhetEntityConsumer, KlassifikasjonssystemConsumer klassifikasjonssystemConsumer, RolleConsumer rolleConsumer, SakstatusConsumer sakstatusConsumer, ArkivdelConsumer arkivdelConsumer, SkjermingshjemmelConsumer skjermingshjemmelConsumer, TilgangsrestriksjonConsumer tilgangsrestriksjonConsumer, KlassifikasjonstypeConsumer klassifikasjonstypeConsumer, DokumentstatusConsumer dokumentstatusConsumer, DokumenttypeConsumer dokumenttypeConsumer) {
        this.administrativEnhetEntityConsumer = administrativEnhetEntityConsumer;
        this.klassifikasjonssystemConsumer = klassifikasjonssystemConsumer;
        this.rolleConsumer = rolleConsumer;
        this.sakstatusConsumer = sakstatusConsumer;
        this.arkivdelConsumer = arkivdelConsumer;
        this.skjermingshjemmelConsumer = skjermingshjemmelConsumer;
        this.tilgangsrestriksjonConsumer = tilgangsrestriksjonConsumer;
        this.klassifikasjonstypeConsumer = klassifikasjonstypeConsumer;
        this.dokumentstatusConsumer = dokumentstatusConsumer;
        this.dokumenttypeConsumer = dokumenttypeConsumer;
    }

    @GetMapping("administrativenhet")
    public Collection<AdministrativEnhetResource> getAdministrativEnheter() {
        return administrativEnhetEntityConsumer.getResourceCache().getValues();
    }

    @GetMapping("klassifikasjonssystem")
    public Collection<KlassifikasjonssystemResource> getKlassifikasjonssystem() {
        return klassifikasjonssystemConsumer.getResourceCache().getValues();
    }

    @GetMapping("rolle")
    public Collection<RolleResource> getRolle() {
        return rolleConsumer.getResourceCache().getValues();
    }

    @GetMapping("sakstatus")
    public Collection<SaksstatusResource> getSakstatus() {
        return sakstatusConsumer.getResourceCache().getValues();
    }

    @GetMapping("arkivdel")
    public Collection<ArkivdelResource> getArkivdel() {
        return arkivdelConsumer.getResourceCache().getValues();
    }

    @GetMapping("skjermingshjemmel")
    public Collection<SkjermingshjemmelResource> getSkjermingshjemmel() {
        return skjermingshjemmelConsumer.getResourceCache().getValues();
    }

    @GetMapping("tilgangsrestriksjon")
    public Collection<TilgangsrestriksjonResource> getTilgangsrestriksjon() {
        return tilgangsrestriksjonConsumer.getResourceCache().getValues();
    }

    @GetMapping("klassifikasjonstype")
    public Collection<KlassifikasjonstypeResource> getKlassifikasjonstype() {
        return klassifikasjonstypeConsumer.getResourceCache().getValues();
    }

    @GetMapping("dokumentstatus")
    public Collection<DokumentStatusResource> getDokumentstatus() {
        return dokumentstatusConsumer.getResourceCache().getValues();
    }

    @GetMapping("dokumenttype")
    public Collection<DokumentTypeResource> getDokumenttype() {
        return dokumenttypeConsumer.getResourceCache().getValues();
    }
}
