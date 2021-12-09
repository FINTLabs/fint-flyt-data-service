package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.kodeverk.DokumentStatusResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import no.fintlabs.kafka.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DokumentstatusConsumer {

    @Getter
    private final ResourceCache<DokumentStatusResource> resourceCache;

    public DokumentstatusConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                dokumentStatusResource -> dokumentStatusResource.getSystemId().getIdentifikatorverdi(),
                mapper,
                DokumentStatusResource.class
        );
    }

    @Bean
    String dokumentstatusTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName("arkiv.kodeverk.dokumentstatus");
    }

    @KafkaListener(topics = "#{dokumentstatusTopicName}")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}