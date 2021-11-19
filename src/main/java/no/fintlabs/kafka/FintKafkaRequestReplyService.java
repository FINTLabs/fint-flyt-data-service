package no.fintlabs.kafka;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
public class FintKafkaRequestReplyService<T> {
    private final ReplyingKafkaTemplate<String, String, T> replyingKafkaTemplate;

    public FintKafkaRequestReplyService(ReplyingKafkaTemplate<String, String, T> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    public T get(String requestTopic, String requestValue) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(requestTopic, requestValue);
        RequestReplyFuture<String, String, T> replyFuture = replyingKafkaTemplate.sendAndReceive(producerRecord);
        SendResult<String, String> sendResult = null;

        try {

            sendResult = replyFuture.getSendFuture().get();
            log.info("Sent ok: " + sendResult.getRecordMetadata());
            ConsumerRecord<String, T> consumerRecord = null;
            consumerRecord = replyFuture.get();
            log.info("Return value: " + consumerRecord.value());

            T value = consumerRecord.value();
            return value;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } /*catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }*/
        return null;
    }
}
