package no.fintlabs.kafka;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class CustomDeserializer implements Deserializer<EntityMessage<AdministrativEnhetResource>> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public EntityMessage<AdministrativEnhetResource> deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");

            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory()
                    .constructParametricType(EntityMessage.class, AdministrativEnhetResource.class);

            return objectMapper.readValue(new String(data, "UTF-8"), type);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }

    @Override
    public void close() {
    }
}