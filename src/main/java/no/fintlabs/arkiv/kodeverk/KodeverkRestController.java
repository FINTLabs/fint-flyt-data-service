package no.fintlabs.arkiv.kodeverk;

import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("kodeverk/")
public class KodeverkRestController {

    private final AdministrativEnhetEntityConsumer administrativEnhetEntityConsumer;

    public KodeverkRestController(AdministrativEnhetEntityConsumer administrativEnhetEntityConsumer) {
        this.administrativEnhetEntityConsumer = administrativEnhetEntityConsumer;
    }

    @GetMapping("administrativenhet")
    public Collection<AdministrativEnhetResource> getAdministrativEnheter() {
        return administrativEnhetEntityConsumer.getResourceCache().getValues();
    }
}
