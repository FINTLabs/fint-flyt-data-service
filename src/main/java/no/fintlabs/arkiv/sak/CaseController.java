package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.arkiv.sak.model.CaseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api/sak")
public class CaseController {

    private final ArchiveCaseFolderIdRequestService archiveCaseFolderIdRequestService;
    private final CaseRequestService caseRequestService;
    private final CaseInfoMappingService caseInfoMappingService;

    public CaseController(
            ArchiveCaseFolderIdRequestService archiveCaseFolderIdRequestService,
            CaseRequestService caseRequestService,
            CaseInfoMappingService caseInfoMappingService
    ) {
        this.archiveCaseFolderIdRequestService = archiveCaseFolderIdRequestService;
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

    @GetMapping("info")
    public ResponseEntity<CaseInfo> getCaseInfo(@RequestParam String sourceApplicationInstanceId) {
        String sourceApplication = "TODO"; // TODO: 16/06/2022 Get from authorisation props? Necessary?
        return archiveCaseFolderIdRequestService.getArchiveCaseFolderId(sourceApplicationInstanceId)
                        .flatMap(caseRequestService::getByMappeId)
                        .map(caseResource -> caseInfoMappingService.toCaseInfo(sourceApplicationInstanceId, caseResource))
                        .map(ResponseEntity::ok)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Case with sourceApplicationInstanceId=%s could not be found", sourceApplicationInstanceId)
                        ));
    }

}
