package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.kafka.FintKafkaRequestReplyUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SakRequestService {

    private final ReplyingKafkaTemplate<String, Object, String> sakReplyingKafkaTemplate;

    public SakRequestService(@Qualifier("sakReplyingKafkaTemplate") ReplyingKafkaTemplate<String, Object, String> sakReplyingKafkaTemplate) {
        this.sakReplyingKafkaTemplate = sakReplyingKafkaTemplate;
    }

    public SakResource getBySystemId(String systemId) {
        return FintKafkaRequestReplyUtil.get(
                SakRequestConfig.requestTopicSystemId,
                systemId,
                sakReplyingKafkaTemplate,
                SakResource.class
        );
    }

    public SakResource getByMappeId(String mappeId) {
        return FintKafkaRequestReplyUtil.get(
                SakRequestConfig.requestTopicMappeId,
                mappeId,
                sakReplyingKafkaTemplate,
                SakResource.class
        );
    }

}
