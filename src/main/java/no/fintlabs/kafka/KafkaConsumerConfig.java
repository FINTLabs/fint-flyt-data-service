//package no.fintlabs.kafka;
//
//
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@EnableKafka
//@Configuration
//public class KafkaConsumerConfig {
//
//    @Bean
//    public ConsumerFactory<String, EntityMessage<AdministrativEnhetResource>> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(
//                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                "localhost:29092");
//        props.put(
//                ConsumerConfig.GROUP_ID_CONFIG,
//                "group_id");
//        props.put(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
////        props.put(
////                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
////                JsonDeserializer.class);
//
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "no.fintlabs.kafka.CustomDeserializer");
//
////        ObjectMapper mapper = new ObjectMapper();
////        JavaType type = mapper.getTypeFactory()
////                .constructParametricType(EntityMessage.class, AdministrativEnhetResource.class);
//
////        EntityMessage<AdministrativEnhetResource> result = new EntityMessage<>();
////        return objectMapper.readValue(new String(data, "UTF-8"), (Class<EntityMessage<AdministrativEnhetResource>>) result.getClass());
//
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new CustomDeserializer());
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, EntityMessage<AdministrativEnhetResource>> kafkaListenerContainerFactory() {
//
//        ConcurrentKafkaListenerContainerFactory<String, EntityMessage<AdministrativEnhetResource>>
//                factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//}
