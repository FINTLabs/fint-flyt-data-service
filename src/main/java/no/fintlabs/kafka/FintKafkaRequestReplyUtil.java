package no.fintlabs.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.ExecutionException;

@Slf4j
public class FintKafkaRequestReplyUtil {

    // TODO: 22/11/2021 Move mapper
    public static final ObjectMapper mapper = new ObjectMapper();

    private FintKafkaRequestReplyUtil() {
    }

    public static <T> T get(
            String requestTopic,
            Object requestValue,
            ReplyingKafkaTemplate<String, Object, String> replyingKafkaTemplate,
            Class<T> replyValueClass
    ) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(requestTopic, requestValue);
        RequestReplyFuture<String, Object, String> replyFuture = replyingKafkaTemplate.sendAndReceive(producerRecord);
        try {
            SendResult<String, Object> sendResult = replyFuture.getSendFuture().get();
            log.info("Sent ok: " + sendResult.getRecordMetadata());
            ConsumerRecord<String, String> consumerRecord = null;
            consumerRecord = replyFuture.get();
            log.info("Return value: " + consumerRecord.value());
            return FintKafkaRequestReplyUtil.mapper.readValue(consumerRecord.value(), replyValueClass);
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
