package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.arkiv.sak.model.AdministrativeUnit;
import no.fintlabs.arkiv.sak.model.CaseInfo;
import no.fintlabs.arkiv.sak.model.CaseManager;
import no.fintlabs.arkiv.sak.model.CaseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class CaseController {

    private final ArchiveCaseIdRequestService archiveCaseIdRequestService;
    private final CaseRequestService caseRequestService;
    private final CaseInfoMappingService caseInfoMappingService;

    public CaseController(
            ArchiveCaseIdRequestService archiveCaseIdRequestService,
            CaseRequestService caseRequestService,
            CaseInfoMappingService caseInfoMappingService
    ) {
        this.archiveCaseIdRequestService = archiveCaseIdRequestService;
        this.caseRequestService = caseRequestService;
        this.caseInfoMappingService = caseInfoMappingService;
    }

    @GetMapping("intern/sakstittel/mappeid/{caseYear}/{caseNumber}")
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

    @GetMapping("sak/instansid/{sourceApplicationInstanceId}")
    public ResponseEntity<CaseInfo> getCaseInfo(
            @PathVariable String sourceApplicationInstanceId,
            @RequestParam Optional<Boolean> returnMockData
    ) {
        if (returnMockData.orElse(false)) {
            return ResponseEntity.ok(createMockCaseInfo(sourceApplicationInstanceId));
        }
        String sourceApplication = "TODO"; // TODO: 16/06/2022 Get from authorisation props? Necessary?
        return archiveCaseIdRequestService.getArchiveCaseId(sourceApplicationInstanceId)
                .flatMap(caseRequestService::getByMappeId)
                .map(caseResource -> caseInfoMappingService.toCaseInfo(sourceApplicationInstanceId, caseResource))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Case with sourceApplicationInstanceId=%s could not be found", sourceApplicationInstanceId)
                ));
    }

    private CaseInfo createMockCaseInfo(String sourceApplicationInstanceId) {
        return CaseInfo
                .builder()
                .sourceApplicationInstanceId(sourceApplicationInstanceId)
                .archiveCaseId("2021/02")
                .caseManager(
                        CaseManager
                                .builder()
                                .firstName("Ola")
                                .middleName(null)
                                .lastName("Nordmann")
                                .email("ola.normann@domain.com")
                                .phone("12345678")
                                .build()
                )
                .administrativeUnit(
                        AdministrativeUnit
                                .builder()
                                .name("VGGLEM Skolemiljø og Kommunikasjon")
                                .build()
                )
                .status(
                        CaseStatus
                                .builder()
                                .name("Under behandling")
                                .code("B")
                                .build()
                )
                .build();
    }

}
