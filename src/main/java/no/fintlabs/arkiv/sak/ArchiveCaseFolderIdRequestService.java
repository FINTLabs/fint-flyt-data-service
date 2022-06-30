package no.fintlabs.arkiv.sak;

import no.fintlabs.kafka.common.topic.TopicCleanupPolicyParameters;
import no.fintlabs.kafka.requestreply.RequestProducer;
import no.fintlabs.kafka.requestreply.RequestProducerFactory;
import no.fintlabs.kafka.requestreply.RequestProducerRecord;
import no.fintlabs.kafka.requestreply.topic.ReplyTopicNameParameters;
import no.fintlabs.kafka.requestreply.topic.ReplyTopicService;
import no.fintlabs.kafka.requestreply.topic.RequestTopicNameParameters;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArchiveCaseFolderIdRequestService {

    private final RequestProducer<String, String> caseIdRequestProducer;
    private final RequestTopicNameParameters requestTopicNameParameters;

    public ArchiveCaseFolderIdRequestService(
            @Value("${fint.kafka.application-id}") String applicationId,
            ReplyTopicService replyTopicService,
            RequestProducerFactory requestProducerFactory
    ) {
        requestTopicNameParameters = RequestTopicNameParameters
                .builder()
                .resource("archive.case.folder.id")
                .parameterName("source-application-instance-id")
                .build();

        ReplyTopicNameParameters replyTopicNameParameters = ReplyTopicNameParameters
                .builder()
                .applicationId(applicationId)
                .resource("archive.case.folder.id")
                .build();
        replyTopicService.ensureTopic(replyTopicNameParameters, 0, TopicCleanupPolicyParameters.builder().build());
        caseIdRequestProducer = requestProducerFactory.createProducer(
                replyTopicNameParameters,
                String.class,
                String.class
        );
    }

    public Optional<String> getArchiveCaseFolderId(String sourceApplicationInstanceId) {
        return caseIdRequestProducer.requestAndReceive(
                RequestProducerRecord
                        .<String>builder()
                        .topicNameParameters(requestTopicNameParameters)
                        .value(sourceApplicationInstanceId)
                        .build()
        ).map(ConsumerRecord::value);
    }
}
