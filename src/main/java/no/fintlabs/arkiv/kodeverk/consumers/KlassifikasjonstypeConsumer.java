package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.kodeverk.KlassifikasjonstypeResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KlassifikasjonstypeConsumer {

    @Getter
    private final ResourceCache<KlassifikasjonstypeResource> resourceCache;

    public KlassifikasjonstypeConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                klassifikasjonstypeResource -> klassifikasjonstypeResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                KlassifikasjonstypeResource.class
        );
    }

    @KafkaListener(topics = "entity.arkiv.kodeverk.klassifikasjonstype")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}