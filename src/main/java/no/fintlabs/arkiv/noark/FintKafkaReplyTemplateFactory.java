package no.fintlabs.arkiv.noark;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.model.resource.arkiv.samferdsel.SoknadDrosjeloyveResource;
import no.fintlabs.kafka.KafkaAdminConfiguration;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
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

        ConcurrentMessageListenerContainer<String, R> repliesContainer =
                containerFactory.createContainer(responseTopic);
        repliesContainer.getContainerProperties().setGroupId("repliesGroup");
        repliesContainer.setAutoStartup(false);

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

}
