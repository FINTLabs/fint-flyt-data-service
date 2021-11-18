package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.kafka.FintKafkaRequestReplyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SakController {

    public static final String baseRequestTopic = "request.arkiv.noark.sak";

    //private final ReplyingKafkaTemplate<String, String, SakResource> replyingKafkaTemplate;
    private final FintKafkaRequestReplyService<SakResource> sakRequestReplyService;

    public SakController(FintKafkaRequestReplyService<SakResource> sakRequestReplyService) {
        this.sakRequestReplyService = sakRequestReplyService;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 200000)
    //@GetMapping("drosjeloyve")
    public void getSak() {

        int systemid = 1414;
        String requestTopic = baseRequestTopic + ".systemid";

        // TODO: 18/11/2021: Where should we initiaze topics
        //new NewTopic(KafkaConfig.requestTopic, 1, (short) 1);
        //new NewTopic(KafkaConfig.responseTopic, 1, (short) 1);

        // TODO: 18/11/2021 Should set default values
        //replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(30));
        //replyingKafkaTemplate.setSharedReplyTopic(true);

        sakRequestReplyService.get(requestTopic, String.valueOf(systemid));
    }
}
