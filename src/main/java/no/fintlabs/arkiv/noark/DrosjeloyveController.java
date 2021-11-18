package no.fintlabs.arkiv.noark;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.samferdsel.SoknadDrosjeloyveResource;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.internals.Topic;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class DrosjeloyveController {

    public DrosjeloyveController(ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory) {
        this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
    }

    private final ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory;

    @PostConstruct
    //@GetMapping("drosjeloyve")
    public void getDrosjeloyve() {

        int systemid = 1414;

        String requestTopic = "request.arkiv.samferdsel.soknaddrosjeloyve.systemid";
        String responseTopic = "response.arkiv.samferdsel.soknaddrosjeloyve";

        new NewTopic(requestTopic, 1, (short) 1);
        new NewTopic(responseTopic, 1, (short) 1);

        ReplyingKafkaTemplate<String, String, Object> template =
                new FintKafkaReplyTemplateFactory<String, Object>(kafkaListenerContainerFactory)
                        .create(String.class, Object.class, responseTopic);
        template.start();
        while (!template.isRunning()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        template.setDefaultReplyTimeout(Duration.ofSeconds(30));
        template.setSharedReplyTopic(true);

        ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic, String.valueOf(systemid));
        RequestReplyFuture<String, String, Object> replyFuture = template.sendAndReceive(record);
        SendResult<String, String> sendResult = null;

        try {

            sendResult = replyFuture.getSendFuture().get();
            log.info("Sent ok: " + sendResult.getRecordMetadata());
            ConsumerRecord<String, Object> consumerRecord = null;
            consumerRecord = replyFuture.get();
            log.info("Return value: " + consumerRecord.value());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
