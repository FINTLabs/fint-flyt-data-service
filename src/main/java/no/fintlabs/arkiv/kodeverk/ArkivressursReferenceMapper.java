package no.fintlabs.arkiv.kodeverk;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.cache.exceptions.NoSuchCacheEntryException;
import no.fintlabs.cache.exceptions.NoSuchCacheException;
import no.fintlabs.links.NoSuchLinkException;
import no.fintlabs.links.ResourceLinkUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
public class ArkivressursReferenceMapper {

    private final FintCache<String, PersonalressursResource> personalressursResourceCache;
    private final FintCache<String, PersonResource> personResourceCache;

    public ArkivressursReferenceMapper(
            FintCache<String, PersonalressursResource> personalressursResourceCache,
            FintCache<String, PersonResource> personResourceCache
    ) {
        this.personalressursResourceCache = personalressursResourceCache;
        this.personResourceCache = personResourceCache;
    }

    public Optional<ResourceReference> map(ArkivressursResource arkivressursResource) {
        try {
            return Optional.of(new ResourceReference(
                    ResourceLinkUtil.getFirstSelfLink(arkivressursResource),
                    this.getDisplayText(arkivressursResource)
            ));
        } catch (NoSuchLinkException | NoSuchCacheException | NoSuchCacheEntryException e) {
            return Optional.empty();
        }
    }

    private String getDisplayText(ArkivressursResource arkivressursResource) {
        String personalressursResourceHref = this.getPersonalressursResourceHref(arkivressursResource);
        PersonalressursResource personalressursResource = personalressursResourceCache.get(personalressursResourceHref);

        String personResourceHref = this.getPersonResourceHref(personalressursResource);
        PersonResource personResource = personResourceCache.get(personResourceHref);

        Personnavn personnavn = personResource.getNavn();
        if (personnavn == null) {
            throw new IllegalStateException("Person resource contains no name");
        }
        String personnavnString = Stream.of(
                        personnavn.getFornavn(),
                        personnavn.getMellomnavn(),
                        personnavn.getEtternavn()
                ).filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        String ansattnummerString = personalressursResource.getAnsattnummer().getIdentifikatorverdi();

        return String.format("%s (%s)", personnavnString, ansattnummerString);
    }

    private String getPersonalressursResourceHref(ArkivressursResource arkivressursResource) {
        return ResourceLinkUtil.getFirstLink(arkivressursResource::getPersonalressurs, arkivressursResource, "Personalressurs");
    }

    private String getPersonResourceHref(PersonalressursResource personalressursResource) {
        return ResourceLinkUtil.getFirstLink(personalressursResource::getPerson, personalressursResource, "Person");
    }

}
