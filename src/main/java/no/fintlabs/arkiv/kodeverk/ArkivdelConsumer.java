package no.fintlabs.arkiv.kodeverk;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ArkivdelConsumer {

    @Getter
    private final ResourceCache<ArkivdelResource> resourceCache;

    public ArkivdelConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                arkivdelResource -> arkivdelResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                ArkivdelResource.class
        );
    }

    @KafkaListener(topics = "entity.arkiv.noark.arkivdel")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}