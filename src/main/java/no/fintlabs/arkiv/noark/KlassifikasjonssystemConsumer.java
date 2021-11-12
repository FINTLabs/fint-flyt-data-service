package no.fintlabs.arkiv.noark;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.kodeverk.KlassifikasjonstypeResource;
import no.fintlabs.kafka.EntityMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KlassifikasjonssystemConsumer {

    @KafkaListener(topics = "arkiv.noark.klassifikasjonssystem")
    public void processMessage(KlassifikasjonstypeResource entityMessage) {
        log.info("Message received by consumer 1: " + entityMessage.toString());
    }
}
