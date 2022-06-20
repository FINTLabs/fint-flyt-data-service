package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.arkiv.sak.model.CaseInfo;
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

    private final CaseIdRequestService caseIdRequestService;
    private final CaseRequestService caseRequestService;
    private final CaseInfoMappingService caseInfoMappingService;

    public CaseController(
            CaseIdRequestService caseIdRequestService,
            CaseRequestService caseRequestService,
            CaseInfoMappingService caseInfoMappingService
    ) {
        this.caseIdRequestService = caseIdRequestService;
        this.caseRequestService = caseRequestService;
        this.caseInfoMappingService = caseInfoMappingService;
    }

    @GetMapping("mappeid/{caseYear}/{caseNumber}/tittel")
    public ResponseEntity<CaseTitle> getCaseTitle(@PathVariable String caseYear, @PathVariable String caseNumber) {
        String mappeId = caseYear + "/" + caseNumber;
        return caseRequestService.getByMappeId(mappeId)
                .map(SakResource::getTittel)
                .map(CaseTitle::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Case with mappeId=%s could not be found", mappeId)
                ));
    }

    @GetMapping("kildesystem-instans-id/{sourceApplicationInstanceId}/info")
    public ResponseEntity<CaseInfo> getCaseInfo(@PathVariable String sourceApplicationInstanceId) {
        String sourceApplication = "TODO"; // TODO: 16/06/2022 Get from authorisation props? Necessary?
        return caseIdRequestService.getCaseId(sourceApplicationInstanceId)
                        .flatMap(caseRequestService::getByMappeId)
                        .map(caseResource -> caseInfoMappingService.toCaseInfo(sourceApplicationInstanceId, caseResource))
                        .map(ResponseEntity::ok)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Case with sourceApplicationInstanceId=%s could not be found", sourceApplicationInstanceId)
                        ));
    }

}
