package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.kodeverk.TilgangsrestriksjonResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TilgangsrestriksjonConsumer {

    @Getter
    private final ResourceCache<TilgangsrestriksjonResource> resourceCache;

    public TilgangsrestriksjonConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                tilgangsrestriksjonResource -> tilgangsrestriksjonResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                TilgangsrestriksjonResource.class
        );
    }

    @KafkaListener(topics = "entity.arkiv.kodeverk.tilgangsrestriksjon")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}