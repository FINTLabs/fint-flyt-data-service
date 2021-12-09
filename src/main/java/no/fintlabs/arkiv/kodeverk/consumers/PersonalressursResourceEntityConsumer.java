package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fintlabs.arkiv.kodeverk.ResourceCache;
import no.fintlabs.kafka.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersonalressursResourceEntityConsumer {

    @Getter
    private final ResourceCache<PersonalressursResource> resourceCache;

    public PersonalressursResourceEntityConsumer(ObjectMapper mapper) {
        this.resourceCache = new ResourceCache<>(
                personalressursResource -> personalressursResource.getAnsattnummer().getIdentifikatorverdi(),
                mapper,
                PersonalressursResource.class
        );
    }

    @Bean
    String personalressulsResourceEntityTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName("administrasjon.personal.personalressurs");
    }

    @KafkaListener(topics = "#{personalressulsResourceEntityTopicName}")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        this.resourceCache.add(consumerRecord);
    }
}
