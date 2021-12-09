package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import no.fintlabs.kafka.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdministrativEnhetEntityConsumer {

    @Getter
    private final ResourceCache<AdministrativEnhetResource> resourceCache;

    public AdministrativEnhetEntityConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                administrativEnhetResource -> administrativEnhetResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                AdministrativEnhetResource.class
        );
    }

    @Bean
    String administrativEnhetTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName("arkiv.noark.administrativenhet");
    }

    @KafkaListener(topics = "#{administrativEnhetTopicName}")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}
