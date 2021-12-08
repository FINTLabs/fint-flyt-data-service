package no.fintlabs.arkiv.kodeverk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class Cache<R> {

    private final Function<R, List<String>> keyMapper;
    private final ObjectMapper mapper;
    private final Class<R> clazz;

    protected final HashMap<String, R> resources;

    private final ArrayList<Consumer<R>> changeCallbacks;
    private final HashMap<String, ArrayList<Consumer<R>>> keyFilteredChangeCallbacks;

    public Cache(Function<R, List<String>> keyMapper, ObjectMapper mapper, Class<R> clazz) {
        this.keyMapper = keyMapper;
        this.mapper = mapper;
        this.resources = new HashMap<>();
        this.clazz = clazz;
        this.changeCallbacks = new ArrayList<>();
        this.keyFilteredChangeCallbacks = new HashMap<>();
    }

    public void add(ConsumerRecord<String, String> consumerRecord) {
        try {
            R resource = mapper.readValue(consumerRecord.value(), clazz);
            log.info("Message received by consumer 1: " + resource.toString());
            this.keyMapper.apply(resource).forEach(key -> resources.put(key, resource));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void notifyCallbacks(String key, R resource) {
        this.changeCallbacks.forEach(callback -> callback.accept(resource));
        this.keyFilteredChangeCallbacks.get(key).forEach(callback -> callback.accept(resource));
    }

    public void registerChangeCallback(Consumer<R> callback) {
        this.changeCallbacks.add(callback);
    }

    public void registerChangeCallback(Consumer<R> callback, String... keyFilters) {
        Arrays.stream(keyFilters).forEach(key -> {
            this.keyFilteredChangeCallbacks.putIfAbsent(key, new ArrayList<>());
            this.keyFilteredChangeCallbacks.get(key).add(callback);
        });
    }

    public Collection<R> getValues() {
        return resources.values();
    }

    public Optional<R> get(String key) {
        return Optional.ofNullable(this.resources.get(key));
    }
}
