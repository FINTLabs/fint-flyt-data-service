package no.fintlabs.kafka;

import no.fint.model.resource.arkiv.noark.SakResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaConfig {
    @Bean
    public ReplyingKafkaTemplate<String, String, SakResource> sakReplyingKafkaTemplate(ConcurrentKafkaListenerContainerFactory<String, SakResource> kafkaListenerContainerFactory) {
        return new FintKafkaReplyTemplateFactory<String, SakResource>(kafkaListenerContainerFactory)
                .create(String.class, SakResource.class, "response.arkiv.noark.sak");
    }
}
