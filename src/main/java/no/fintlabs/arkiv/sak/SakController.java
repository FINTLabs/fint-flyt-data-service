package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.arkiv.sak.model.SakDTO;
import no.fintlabs.kafka.TopicCleanupPolicyParameters;
import no.fintlabs.kafka.requestreply.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("sak")
public class SakController {

    private final RequestProducer<String, SakResource> requestProducer;
    private final SakMapper sakMapper;
    private final String orgId;

    public SakController(
            ReplyTopicService replyTopicService,
            FintKafkaRequestProducerFactory fintKafkaRequestProducerFactory,
            SakMapper sakMapper,
            @Value("${fint.org-id}") String orgId,
            @Value("${fint.kafka.application-id}") String applicationId
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

        this.sakMapper = sakMapper;
        this.orgId = orgId;
    }

    @GetMapping("mappeid/{caseYear}/{caseNumber}")
    public SakDTO getSak(@PathVariable String caseYear, @PathVariable String caseNumber) {
        String mappeId = caseYear + "/" + caseNumber;
        SakResource sakResource = requestProducer.requestAndReceive(
                        new RequestProducerRecord<>(
                                RequestTopicNameParameters.builder()
                                        .orgId(orgId)
                                        .domainContext("skjema")
                                        .resource("arkiv.noark.sak")
                                        .parameterName("mappeid")
                                        .build(),
                                null,
                                mappeId
                        )
                )
                .map(ConsumerRecord::value)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Case with mappeId=%s not found", mappeId)
                ));
        return this.sakMapper.toSakDTO(sakResource);
    }

}
