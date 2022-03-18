package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.kafka.TopicCleanupPolicyParameters;
import no.fintlabs.kafka.requestreply.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CaseRequestService {

    private final RequestProducer<String, SakResource> requestProducer;
    private final RequestTopicNameParameters requestTopicNameParameters;

    public CaseRequestService(
            @Value("${fint.org-id}") String orgId,
            @Value("${fint.kafka.application-id}") String applicationId,
            ReplyTopicService replyTopicService,
            FintKafkaRequestProducerFactory fintKafkaRequestProducerFactory
    ) {
        ReplyTopicNameParameters replyTopicNameParameters = ReplyTopicNameParameters.builder()
                .orgId(orgId)
                .domainContext("skjema")
                .applicationId(applicationId)
                .resource("arkiv.noark.sak")
                .build();

        replyTopicService.ensureTopic(replyTopicNameParameters, 0, TopicCleanupPolicyParameters.builder().build());

        this.requestProducer = fintKafkaRequestProducerFactory.createProducer(
                replyTopicNameParameters,
                String.class,
                SakResource.class
        );

        requestTopicNameParameters = RequestTopicNameParameters.builder()
                .orgId(orgId)
                .domainContext("skjema")
                .resource("arkiv.noark.sak")
                .parameterName("mappeid")
                .build();
    }

    public Optional<SakResource> getByMappeId(String mappeId) {
        return requestProducer.requestAndReceive(
                RequestProducerRecord.<String>builder()
                        .topicNameParameters(requestTopicNameParameters)
                        .value(mappeId)
                        .build()
        ).map(ConsumerRecord::value);
    }

}
