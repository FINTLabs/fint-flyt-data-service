package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import no.fintlabs.kafka.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// TODO: 02/12/2021 Rename all consumers with 'Resource' and 'Entity' suffix
@Slf4j
@Component
public class ArkivressursResourceEntityConsumer {

    @Getter
    private final ResourceCache<ArkivressursResource> resourceCache;

    public ArkivressursResourceEntityConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                arkivressursResource -> arkivressursResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                ArkivressursResource.class
        );
    }

    @Bean
    String arkivressursResourceEntityTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName("arkiv.noark.arkivressurs");
    }

    @KafkaListener(topics = "#{arkivressursResourceEntityTopicName}")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}
