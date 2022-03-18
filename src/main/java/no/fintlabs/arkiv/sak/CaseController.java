package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.arkiv.sak.model.Case;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api/sak")
public class CaseController {

    private final CaseRequestService caseRequestService;
    private final CaseMapper caseMapper;

    public CaseController(
            CaseRequestService caseRequestService,
            CaseMapper caseMapper
    ) {
        this.caseMapper = caseMapper;
        this.caseRequestService = caseRequestService;
    }

    @GetMapping("mappeid/{caseYear}/{caseNumber}")
    public ResponseEntity<Case> getSak(@PathVariable String caseYear, @PathVariable String caseNumber) {
        String mappeId = caseYear + "/" + caseNumber;
        SakResource sakResource = caseRequestService.getByMappeId(mappeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Case with mappeId=%s not found", mappeId)
                ));
        return ResponseEntity.ok(caseMapper.toCase(sakResource));
    }
}
