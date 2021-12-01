package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.kodeverk.RolleResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import no.fintlabs.kafka.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RolleConsumer {

    @Getter
    private final ResourceCache<RolleResource> resourceCache;

    public RolleConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                rolleResource -> rolleResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                RolleResource.class
        );
    }

    @Bean
    String rolleTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName("arkiv.kodeverk.rolle");
    }

    @KafkaListener(topics = "#{rolleTopicName}")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }

}