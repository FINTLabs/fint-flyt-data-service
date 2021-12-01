package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.kafka.util.FintKafkaRequestReplyUtil;
import no.fintlabs.kafka.util.RequestReplyOperationArgs;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SakRequestService {

    private final ReplyingKafkaTemplate<String, Object, String> sakReplyingKafkaTemplate;
    private final NewTopic sakRequestTopicSystemId;
    private final NewTopic sakRequestTopicMappeId;

    public SakRequestService(
            @Qualifier("sakReplyingKafkaTemplate") ReplyingKafkaTemplate<String, Object, String> sakReplyingKafkaTemplate,
            @Qualifier("sakRequestTopicSystemId") NewTopic sakRequestTopicSystemId,
            @Qualifier("sakRequestTopicMappeId") NewTopic sakRequestTopicMappeId
    ) {
        this.sakReplyingKafkaTemplate = sakReplyingKafkaTemplate;
        this.sakRequestTopicSystemId = sakRequestTopicSystemId;
        this.sakRequestTopicMappeId = sakRequestTopicMappeId;
    }

    public SakResource getBySystemId(String systemId) {
        return FintKafkaRequestReplyUtil.get(new RequestReplyOperationArgs<>(
                this.sakRequestTopicSystemId.name(),
                systemId,
                sakReplyingKafkaTemplate,
                SakResource.class
        ));
    }

    public SakResource getByMappeId(String mappeId) {
        return FintKafkaRequestReplyUtil.get(new RequestReplyOperationArgs<>(
                this.sakRequestTopicMappeId.name(),
                mappeId,
                sakReplyingKafkaTemplate,
                SakResource.class
        ));
    }

}
