package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import no.fintlabs.kafka.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KlassifikasjonssystemConsumer {
    @Getter
    private final ResourceCache<KlassifikasjonssystemResource> resourceCache;

    public KlassifikasjonssystemConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                klassifikasjonssystemResource -> klassifikasjonssystemResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                KlassifikasjonssystemResource.class
        );
    }

    @Bean
    String klassifikasjonssystemTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName("arkiv.noark.klassifikasjonssystem");
    }

    @KafkaListener(topics = "#{klassifikasjonssystemTopicName}")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }

}
