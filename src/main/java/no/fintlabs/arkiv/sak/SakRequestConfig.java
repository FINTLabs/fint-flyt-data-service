package no.fintlabs.arkiv.sak;

import no.fintlabs.kafka.FintKafkaReplyTemplateFactory;
import no.fintlabs.kafka.topic.DomainContext;
import no.fintlabs.kafka.topic.TopicService;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.time.Duration;

@Configuration
public class SakRequestConfig {

    private static final String sakResourceReference = "arkiv.noark.sak";

    @Bean
    @Qualifier("sakRequestTopicMappeId")
    public TopicDescription sakRequestTopicMappeId(TopicService topicService) {
        return topicService.getOrCreateRequestTopic(DomainContext.SKJEMA, sakResourceReference, false, "mappeid");
    }

    @Bean
    @Qualifier("sakReplyTopic")
    public TopicDescription sakReplyTopic(TopicService topicService) {
        return topicService.getOrCreateReplyTopic(DomainContext.SKJEMA, sakResourceReference);
    }

    @Bean
    @Qualifier("sakReplyingKafkaTemplate")
    public ReplyingKafkaTemplate<String, String, String> sakReplyingKafkaTemplate(
            @Qualifier("sakReplyTopic") TopicDescription sakReplyTopic,
            ProducerFactory<String, String> producerFactory,
            ConsumerFactory<String, String> consumerFactory
    ) {
        ReplyingKafkaTemplate<String, String, String> sakReplyingKafkaTemplate = FintKafkaReplyTemplateFactory.create(
                producerFactory,
                consumerFactory,
                sakReplyTopic.name()
        );
        sakReplyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(30));
        sakReplyingKafkaTemplate.setSharedReplyTopic(true);
        return sakReplyingKafkaTemplate;
    }

}
