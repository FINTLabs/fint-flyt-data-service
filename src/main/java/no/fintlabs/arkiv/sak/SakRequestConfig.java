package no.fintlabs.arkiv.sak;

import no.fintlabs.kafka.FintKafkaReplyTemplateFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SakRequestConfig {

    public static final String requestTopicSystemId = "request.arkiv.noark.sak.systemid";
    public static final String requestTopicMappeId = "request.arkiv.noark.sak.mappeid";

    @Bean
    public NewTopic requestTopicSystemId() {
        return TopicBuilder.name(requestTopicSystemId)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic requestTopicMappeId() {
        return TopicBuilder.name(requestTopicMappeId)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic replyTopic() {
        return TopicBuilder.name("reply.arkiv.noark.sak")
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    @Qualifier("sakReplyingKafkaTemplate")
    public ReplyingKafkaTemplate<String, Object, String> sakReplyingKafkaTemplate() {
        ReplyingKafkaTemplate<String, Object, String> sakReplyingKafkaTemplate = FintKafkaReplyTemplateFactory.create(
                this.createProducerConfigs(),
                this.createConsumerConfigs(),
                "reply.arkiv.noark.sak"
        );
        sakReplyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(30));
        sakReplyingKafkaTemplate.setSharedReplyTopic(true);
        return sakReplyingKafkaTemplate;
    }

    public Map<String, Object> createProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

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
