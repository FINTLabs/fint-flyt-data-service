package no.fintlabs.arkiv.noark;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.arkiv.samferdsel.SoknadDrosjeloyveResource;
import no.fintlabs.kafka.KafkaAdminConfiguration;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

public class FintKafkaReplyTemplateFactory<V, R> {

    private final ConcurrentKafkaListenerContainerFactory<String, R> containerFactory;

    public FintKafkaReplyTemplateFactory(ConcurrentKafkaListenerContainerFactory<String, R> containerFactory) {
        this.containerFactory = containerFactory;
    }

    public ReplyingKafkaTemplate<String, V, R> create(Class<V> requestValueClass, Class<R> responseValueClass, String responseTopic) {

        ObjectMapper mapper = new ObjectMapper();
        JavaType requestValueType = mapper.getTypeFactory().constructType(requestValueClass);

        ProducerFactory<String, V> pf = new DefaultKafkaProducerFactory<>(
                producerConfigs(),
                new StringSerializer(),
                new JsonSerializer<>(requestValueType, mapper)
        );

        //ConcurrentMessageListenerContainer<String, R> repliesContainer =
        //        containerFactory.createContainer(responseTopic);
        //repliesContainer.getContainerProperties().setGroupId("repliesGroup");

        JavaType replyValueType = mapper.getTypeFactory().constructType(responseValueClass);
        DefaultKafkaConsumerFactory<String, R> cf = new DefaultKafkaConsumerFactory<>(
                consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(replyValueType)
        );

        ContainerProperties containerProperties = new ContainerProperties(responseTopic);
        KafkaMessageListenerContainer<String, R> repliesContainer = new KafkaMessageListenerContainer<>(cf, containerProperties);
        //repliesContainer.setAutoStartup(false);

        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

}
