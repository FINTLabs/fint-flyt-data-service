package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.kafka.util.FintKafkaRequestReplyUtil;
import no.fintlabs.kafka.util.RequestReplyOperationArgs;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SakRequestService {

    private final ReplyingKafkaTemplate<String, String, String> sakReplyingKafkaTemplate;
    private final TopicDescription sakRequestTopicMappeId;

    public SakRequestService(
            @Qualifier("sakReplyingKafkaTemplate") ReplyingKafkaTemplate<String, String, String> sakReplyingKafkaTemplate,
            @Qualifier("sakRequestTopicMappeId") TopicDescription sakRequestTopicMappeId
    ) {
        this.sakReplyingKafkaTemplate = sakReplyingKafkaTemplate;
        this.sakRequestTopicMappeId = sakRequestTopicMappeId;
    }

    public Optional<SakResource> getByMappeId(String mappeId) {
        return FintKafkaRequestReplyUtil.get(new RequestReplyOperationArgs<>(
                this.sakRequestTopicMappeId.name(),
                mappeId,
                sakReplyingKafkaTemplate,
                SakResource.class
        ));
    }

}
