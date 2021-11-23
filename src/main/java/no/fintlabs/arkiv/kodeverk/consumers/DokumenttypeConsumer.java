package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.kodeverk.DokumentTypeResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DokumenttypeConsumer {

    @Getter
    private final ResourceCache<DokumentTypeResource> resourceCache;

    public DokumenttypeConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                dokumentTypeResource -> dokumentTypeResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                DokumentTypeResource.class
        );
    }

    @KafkaListener(topics = "entity.arkiv.kodeverk.dokumenttype")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}