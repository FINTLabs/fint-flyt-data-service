package no.fintlabs.arkiv.kodeverk.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.Link;
import no.fint.model.resource.arkiv.noark.KlasseResource;
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
public class KlasseResourceEntityConsumer extends EntityConsumer<KlasseResource> {

    protected KlasseResourceEntityConsumer(ObjectMapper objectMapper, FintCacheManager fintCacheManager) {
        super(objectMapper, fintCacheManager);
    }

    @Override
    protected String getResourceReference() {
        return "arkiv.noark.klasse";
    }

    @Override
    protected Class<KlasseResource> getResourceClass() {
        return KlasseResource.class;
    }

    @Override
    protected List<String> getKeys(KlasseResource resource) {
        return Stream.concat(
                Stream.of(resource.getKlasseId()),
                resource.getSelfLinks().stream().map(Link::getHref)
        ).collect(Collectors.toList());
    }

    @Bean
    String klasseResourceEntityTopicName(TopicNameService topicNameService) {
        return topicNameService.generateEntityTopicName(DomainContext.SKJEMA, this.getResourceReference());
    }

    @Override
    @KafkaListener(topics = "#{klasseResourceEntityTopicName}")
    protected void consume(ConsumerRecord<String, String> consumerRecord) {
        this.processMessage(consumerRecord);
    }

}
