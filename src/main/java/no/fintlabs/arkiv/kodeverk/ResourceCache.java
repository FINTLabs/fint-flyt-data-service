package no.fintlabs.arkiv.kodeverk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

@Slf4j
public class ResourceCache<R> {

    private final Function<R, String> keyMapper;
    private final ObjectMapper mapper;
    private final Class<R> clazz;
    private final HashMap<String, R> resources;

    public ResourceCache(Function<R, String> keyMapper, ObjectMapper mapper, Class<R> clazz) {
        this.keyMapper = keyMapper;
        this.mapper = mapper;
        this.resources = new HashMap<>();
        this.clazz = clazz;
    }

    protected void add(ConsumerRecord<String, String> consumerRecord) {
        R value = null;
        try {
            value = mapper.readValue(consumerRecord.value(), clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("Message received by consumer 1: " + value.toString());
        resources.put(this.keyMapper.apply(value), value);
    }

    public Collection<R> getValues() {
        return resources.values();
    }
}
