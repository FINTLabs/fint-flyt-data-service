package no.fintlabs.arkiv.kodeverk;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.cache.exceptions.NoSuchCacheEntryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ArkivressursDisplayNameMapperTest {

    @Mock
    FintCache<String, PersonalressursResource> personalressursResourceCache;

    @Mock
    FintCache<String, PersonResource> personResourceCache;

    ArkivressursDisplayNameMapper arkivressursDisplayNameMapper;

    @BeforeEach
    public void setUp() {
        arkivressursDisplayNameMapper = new ArkivressursDisplayNameMapper(
                personalressursResourceCache,
                personResourceCache
        );
    }

    private ArkivressursResource setupMocksForPersonnavn(Personnavn personnavn) {
        ArkivressursResource arkivressursResource = mock(ArkivressursResource.class);
        when(arkivressursResource.getPersonalressurs()).thenReturn(List.of(Link.with("testPersonalressursLink1")));

        PersonalressursResource personalressursResource = mock(PersonalressursResource.class);
        when(personalressursResourceCache.get("testPersonalressursLink1")).thenReturn(personalressursResource);
        when(personalressursResource.getPerson()).thenReturn(List.of(Link.with("testPersonLink1")));

        PersonResource personResource = mock(PersonResource.class);
        when(personResourceCache.get("testPersonLink1")).thenReturn(personResource);

        when(personResource.getNavn()).thenReturn(personnavn);


        return arkivressursResource;
    }

    private ArkivressursResource setupMocksForPersonBrukernavn() {
        ArkivressursResource arkivressursResource = mock(ArkivressursResource.class);
        when(arkivressursResource.getPersonalressurs()).thenReturn(List.of(Link.with("testPersonalressursLink1")));

        PersonalressursResource personalressursResource = mock(PersonalressursResource.class);
        when(personalressursResourceCache.get("testPersonalressursLink1")).thenReturn(personalressursResource);

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi("12345");
        when(personalressursResource.getBrukernavn()).thenReturn(identifikator);

        return arkivressursResource;

    }

    @Test
    public void findPersonNavn_givenFirstMiddleAndLastName_shouldReturnFullNameWithSpaceSeparations() {
        Personnavn personnavn = mock(Personnavn.class);
        when(personnavn.getFornavn()).thenReturn("testFornavn");
        when(personnavn.getMellomnavn()).thenReturn("testMellomnavn");
        when(personnavn.getEtternavn()).thenReturn("testEtternavn");

        ArkivressursResource arkivressursResource = setupMocksForPersonnavn(personnavn);

        Optional<String> personNavn = arkivressursDisplayNameMapper.findPersonNavn(arkivressursResource);

        assertThat(personNavn).isPresent();
        assertThat(personNavn).contains("testFornavn testMellomnavn testEtternavn");
    }

    @Test
    public void findPersonNavn_givenFirstAndLastName_shouldReturnFullNameWithSpaceSeparations() {
        Personnavn personnavn = mock(Personnavn.class);
        when(personnavn.getFornavn()).thenReturn("testFornavn");
        when(personnavn.getMellomnavn()).thenReturn(null);
        when(personnavn.getEtternavn()).thenReturn("testEtternavn");

        ArkivressursResource arkivressursResource = setupMocksForPersonnavn(personnavn);

        Optional<String> personNavn = arkivressursDisplayNameMapper.findPersonNavn(arkivressursResource);

        assertThat(personNavn).isPresent();
        assertThat(personNavn).contains("testFornavn testEtternavn");
    }

    @Test
    public void findPersonNavn_givenNoName_shouldReturnNoName() {
        Personnavn personnavn = mock(Personnavn.class);
        when(personnavn.getFornavn()).thenReturn(null);
        when(personnavn.getMellomnavn()).thenReturn(null);
        when(personnavn.getEtternavn()).thenReturn(null);

        ArkivressursResource arkivressursResource = setupMocksForPersonnavn(personnavn);

        Optional<String> personNavn = arkivressursDisplayNameMapper.findPersonNavn(arkivressursResource);

        assertThat(personNavn).isPresent();
        assertThat(personNavn).contains("");
    }

    @Test
    public void findPersonNavn_givenNullName_shouldThrowIllegalStateException() {
        ArkivressursResource arkivressursResource = setupMocksForPersonnavn(null);

        IllegalStateException illegalStateException = assertThrows(
                IllegalStateException.class,
                () -> arkivressursDisplayNameMapper.findPersonNavn(arkivressursResource)
        );

        assertThat(illegalStateException).hasMessage("Person resource contains no name");
    }

    @Test
    public void findPersonNavn_givenNoMatchingPersonalressursInCache_shouldReturnEmpty() {
        ArkivressursResource arkivressursResource = mock(ArkivressursResource.class);
        when(arkivressursResource.getPersonalressurs()).thenReturn(List.of(Link.with("testPersonalressursLink1")));

        when(personalressursResourceCache.get("testPersonalressursLink1")).thenThrow(
                new NoSuchCacheEntryException("testPersonalressursLink1")
        );

        Optional<String> personNavn = arkivressursDisplayNameMapper.findPersonNavn(arkivressursResource);

        assertThat(personNavn).isEmpty();
    }

    @Test
    public void findPersonNavn_givenNoMatchingPersonInCache_shouldReturnEmpty() {
        ArkivressursResource arkivressursResource = mock(ArkivressursResource.class);
        when(arkivressursResource.getPersonalressurs()).thenReturn(List.of(Link.with("testPersonalressursLink1")));

        PersonalressursResource personalressursResource = mock(PersonalressursResource.class);
        when(personalressursResourceCache.get("testPersonalressursLink1")).thenReturn(personalressursResource);
        when(personalressursResource.getPerson()).thenReturn(List.of(Link.with("testPersonLink1")));

        when(personResourceCache.get("testPersonLink1")).thenThrow(
                new NoSuchCacheEntryException("testPersonLink1")
        );

        Optional<String> personNavn = arkivressursDisplayNameMapper.findPersonNavn(arkivressursResource);

        assertThat(personNavn).isEmpty();
    }

    @Test
    public void findPersonNavn_shouldUseFirstLinksToGetFromCache() {
        ArkivressursResource arkivressursResource = mock(ArkivressursResource.class);
        when(arkivressursResource.getPersonalressurs()).thenReturn(List.of(
                Link.with("testPersonalressursLink1"),
                Link.with("testPersonalressursLink2"),
                Link.with("testPersonalressursLink3")
        ));

        PersonalressursResource personalressursResource = mock(PersonalressursResource.class);
        when(personalressursResourceCache.get("testPersonalressursLink1")).thenReturn(personalressursResource);
        when(personalressursResource.getPerson()).thenReturn(List.of(
                Link.with("testPersonLink1"),
                Link.with("testPersonLink2"),
                Link.with("testPersonLink3")
        ));
        PersonResource personResource = mock(PersonResource.class);
        when(personResourceCache.get("testPersonLink1")).thenReturn(personResource);

        Personnavn personnavn = mock(Personnavn.class);
        when(personnavn.getFornavn()).thenReturn(null);
        when(personnavn.getMellomnavn()).thenReturn(null);
        when(personnavn.getEtternavn()).thenReturn(null);
        when(personResource.getNavn()).thenReturn(personnavn);

        arkivressursDisplayNameMapper.findPersonNavn(arkivressursResource);

        verify(personalressursResourceCache, times(1)).get("testPersonalressursLink1");
        verifyNoMoreInteractions(personalressursResourceCache);

        verify(personResourceCache, times(1)).get("testPersonLink1");
        verifyNoMoreInteractions(personResourceCache);
    }

    @Test
    public void getPersonalressursBrukernavn_shouldReturnIdentifikator() {
        ArkivressursResource arkivressursResource = setupMocksForPersonBrukernavn();

        Optional<String> personBrukernavn = arkivressursDisplayNameMapper.findPersonalressursBrukernavn(arkivressursResource);

        assertThat(personBrukernavn).isPresent();
        assertThat(personBrukernavn).contains("12345");
    }

    @Test
    public void getPersonalressursBrukernavn_shouldReturnEmpty() {
        ArkivressursResource arkivressursResource = mock(ArkivressursResource.class);
        when(arkivressursResource.getPersonalressurs()).thenReturn(List.of(Link.with("testPersonalressursLink1")));

        when(personalressursResourceCache.get("testPersonalressursLink1")).thenThrow(
                new NoSuchCacheEntryException("testPersonalressursLink1")
        );

        Optional<String> personBrukernavn = arkivressursDisplayNameMapper.findPersonalressursBrukernavn(arkivressursResource);

        assertThat(personBrukernavn).isEmpty();
    }

}
