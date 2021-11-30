package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.kodeverk.SaksstatusResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaksstatusConsumer {

    @Getter
    private final ResourceCache<SaksstatusResource> resourceCache;

    public SaksstatusConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                saksstatusResource -> saksstatusResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                SaksstatusResource.class
        );
    }

    @KafkaListener(topics = "entity.arkiv.kodeverk.saksstatus")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}
