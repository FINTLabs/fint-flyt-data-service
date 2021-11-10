package no.fintlabs.arkiv.noark;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fintlabs.kafka.EntityMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdministrativEnhetEntityConsumer {

    @KafkaListener(topics = "arkiv.noark.administrativenhet")
    public void processMessage(EntityMessage<AdministrativEnhetResource> administrativEnhetResource) {
        log.info("Message received by consumer 1: " + administrativEnhetResource.toString());
    }
}
