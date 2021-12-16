package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fintlabs.kafka.consumer.EntityConsumer;
import no.fintlabs.kafka.consumer.cache.FintCacheManager;
import no.fintlabs.kafka.topic.DomainContext;
import no.fintlabs.kafka.topic.TopicNameService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PersonalressursResourceEntityConsumer extends EntityConsumer<PersonalressursResource> {

    protected PersonalressursResourceEntityConsumer(ObjectMapper objectMapper, FintCacheManager fintCacheManager) {
        super(objectMapper, fintCacheManager);
    }

    @Override
    protected String getResourceReference() {
        return "administrasjon.personal.personalressurs";
    }

    @Override
    protected Class<PersonalressursResource> getResourceClass() {
        return PersonalressursResource.class;
    }

    @Override
    protected List<String> getKeys(PersonalressursResource resource) {
        return Stream.concat(
                Stream.of(resource.getAnsattnummer().getIdentifikatorverdi()),
                resource.getSelfLinks().stream().map(Link::getHref)
        ).collect(Collectors.toList());
    }

    @Bean
    String personalressursResourceEntityTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName(DomainContext.SKJEMA, this.getResourceReference());
    }

    @Override
    @KafkaListener(topics = "#{personalressursResourceEntityTopicName}")
    protected void consume(ConsumerRecord<String, String> consumerRecord) {
        super.processMessage(consumerRecord);
    }

}
