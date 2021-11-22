package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SakController {

    private final SakRequestService sakRequestService;

    public SakController(SakRequestService sakRequestService) {
        this.sakRequestService = sakRequestService;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 200000)
    //@GetMapping("drosjeloyve")
    public SakResource getSak() {
        return sakRequestService.getBySystemId(String.valueOf(1414));
    }

}
