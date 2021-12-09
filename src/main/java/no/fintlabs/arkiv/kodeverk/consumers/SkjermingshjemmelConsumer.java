package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.kodeverk.SkjermingshjemmelResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import no.fintlabs.kafka.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SkjermingshjemmelConsumer {

    @Getter
    private final ResourceCache<SkjermingshjemmelResource> resourceCache;

    public SkjermingshjemmelConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                skjermingshjemmelResource -> skjermingshjemmelResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                SkjermingshjemmelResource.class
        );
    }

    @Bean
    String skjermingshjemmelTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName("arkiv.kodeverk.skjermingshjemmel");
    }

    @KafkaListener(topics = "#{skjermingshjemmelTopicName}")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }

}