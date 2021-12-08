package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import no.fintlabs.kafka.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersonResourceEntityConsumer {

    @Getter
    private final ResourceCache<PersonResource> resourceCache;

    public PersonResourceEntityConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                personalressursResource -> personalressursResource.getFodselsnummer().getIdentifikatorverdi(),
                mapper,
                PersonResource.class
        );
    }

    @Bean
    String personResourceEntityTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName("administrasjon.personal.person");
    }

    @KafkaListener(topics = "#{personResourceEntityTopicName}")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}
