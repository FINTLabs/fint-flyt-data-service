package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.MappeResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api/sak")
public class CaseController {

    private final CaseRequestService caseRequestService;

    public CaseController(CaseRequestService caseRequestService) {
        this.caseRequestService = caseRequestService;
    }

    @GetMapping("mappeid/{caseYear}/{caseNumber}/tittel")
    public ResponseEntity<String> getCaseTitle(@PathVariable String caseYear, @PathVariable String caseNumber) {
        String mappeId = caseYear + "/" + caseNumber;
        String caseTitle = caseRequestService.getByMappeId(mappeId)
                .map(MappeResource::getTittel)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Case with mappeId=%s not found", mappeId)
                ));
        return ResponseEntity.ok(caseTitle);
    }

}
