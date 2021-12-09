package no.fintlabs.arkiv.sak;

import no.fintlabs.kafka.FintKafkaReplyTemplateFactory;
import no.fintlabs.kafka.TopicService;
import no.fintlabs.kafka.util.FintKafkaRequestReplyUtil;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SakRequestConfig {

    private static final String sakResourceReference = "arkiv.noark.sak";

    @Bean
    @Qualifier("sakRequestTopicMappeId")
    public NewTopic sakRequestTopicMappeId(TopicService topicService) {
        return topicService.createRequestTopic(sakResourceReference, "mappeid");
    }

    @Bean
    @Qualifier("sakReplyTopic")
    public NewTopic sakReplyTopic(TopicService topicService) {
        return topicService.createReplyTopic(sakResourceReference);
    }
//
//    @Bean
//    public KafkaTemplate<?, ?> kafkaTemplate(
//            return new KafkaTemplate();
//    )

    @Bean
    @Qualifier("sakReplyingKafkaTemplate")
    public ReplyingKafkaTemplate<String, Object, String> sakReplyingKafkaTemplate(
            @Qualifier("sakReplyTopic") NewTopic sakReplyTopic
    ) {
        ReplyingKafkaTemplate<String, Object, String> sakReplyingKafkaTemplate = FintKafkaReplyTemplateFactory.create(
                this.createProducerConfigs(),
                this.createConsumerConfigs(),
                sakReplyTopic.name()
        );
        sakReplyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(30));
        sakReplyingKafkaTemplate.setSharedReplyTopic(true);
        return sakReplyingKafkaTemplate;
    }

//    private ReplyingKafkaTemplate<String, String, String> create(
//            Map<String, Object> producerConfigs,
//            Map<String, Object> consumerConfigs,
//            String replyTopic
//    ) {
//
//        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs);
//        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfigs);
//
//        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
//        ConcurrentMessageListenerContainer<String, String> repliesContainer =
//                new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
//
//        return new ReplyingKafkaTemplate<>(producerFactory, repliesContainer);
//    }

    // TODO: 22/11/2021 Move to YAML
    public Map<String, Object> createProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    // TODO: 22/11/2021 Move to YAML
    public Map<String, Object> createConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroup");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

}
