package no.fintlabs.arkiv.noark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdministrativEnhetEntityConsumer {

//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @KafkaListener(topics = "entity.arkiv.noark.administrativenhet")
//    public void processMessage(ConsumerRecord<String, String> row) throws JsonProcessingException {
//        AdministrativEnhetResource value = mapper.readValue(row.value(), AdministrativEnhetResource.class);
//        log.info("Message received by consumer 1: " + value.toString());
//    }
}
