package no.fintlabs.arkiv.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.SakResource;
import no.fintlabs.arkiv.sak.model.AdministrativeUnit;
import no.fintlabs.arkiv.sak.model.CaseInfo;
import no.fintlabs.arkiv.sak.model.CaseManager;
import no.fintlabs.arkiv.sak.model.CaseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static no.fintlabs.resourceserver.UrlPaths.EXTERNAL_API;
import static no.fintlabs.resourceserver.UrlPaths.INTERNAL_API;
import static no.fintlabs.resourceserver.security.client.ClientAuthorizationUtil.getSourceApplicationId;

@Slf4j
@RestController
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

    @GetMapping(INTERNAL_API + "/sakstittel/mappeid/{caseYear}/{caseNumber}")
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

    @GetMapping(EXTERNAL_API + "/sak/instansid/{sourceApplicationInstanceId}")
    public Mono<ResponseEntity<?>> getCaseInfo(
            @AuthenticationPrincipal Mono<Authentication> authenticationMono,
            @PathVariable String sourceApplicationInstanceId,
            @RequestParam Optional<Boolean> returnMockData
    ) {
        return authenticationMono.map(authentication -> getCaseInfo(
                authentication,
                sourceApplicationInstanceId,
                returnMockData.orElse(false)
        ));
    }

    public ResponseEntity<CaseInfo> getCaseInfo(
            Authentication authentication,
            String sourceApplicationInstanceId,
            boolean returnMockData
    ) {
        if (returnMockData) {
            return ResponseEntity.ok(createMockCaseInfo(sourceApplicationInstanceId));
        }
        return archiveCaseIdRequestService.getArchiveCaseId(
                        getSourceApplicationId(authentication),
                        sourceApplicationInstanceId
                )
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
