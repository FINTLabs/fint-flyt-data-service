package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.arkiv.sak.model.SakDTO;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("sak")
public class SakController {

    private final SakRequestService sakRequestService;
    private final SakMapper sakMapper;

    public SakController(SakRequestService sakRequestService, SakMapper sakMapper) {
        this.sakRequestService = sakRequestService;
        this.sakMapper = sakMapper;
    }

    @Scheduled(initialDelay = 150000, fixedDelay = 200000)
    public void test() {
        this.getSak("21", "1");
    }

    @GetMapping("mappeid/{caseYear}/{caseNumber}")
    public SakDTO getSak(@PathVariable String caseYear, @PathVariable String caseNumber) {
        String mappeId = caseYear + "/" + caseNumber;
        SakResource sakResource = sakRequestService.getByMappeId(mappeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Case with mappeId=%s not found", mappeId)
                ));
        return this.sakMapper.toSakDTO(sakResource);
    }

}
