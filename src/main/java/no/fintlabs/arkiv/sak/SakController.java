package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("sak")
public class SakController {

    private final SakRequestService sakRequestService;

    public SakController(SakRequestService sakRequestService) {
        this.sakRequestService = sakRequestService;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 200000)
    public void test() {
        this.getSak("21", "1");
    }

    // TODO: 24/11/2021 Filter only on mappeid  mappeid/20/12456
    @GetMapping("mappeid/{caseYear}/{caseNumber}")
    public SakResource getSak(@PathVariable String caseYear, @PathVariable String caseNumber) {
        return sakRequestService.getByMappeId(caseYear + '/' + caseNumber);
    }

}
