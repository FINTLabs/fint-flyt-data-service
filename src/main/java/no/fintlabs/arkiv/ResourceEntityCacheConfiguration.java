package no.fintlabs.arkiv;

import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.*;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.cache.FintCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class ResourceEntityCacheConfiguration {

    private final FintCacheManager fintCacheManager;

    public ResourceEntityCacheConfiguration(FintCacheManager fintCacheManager) {
        this.fintCacheManager = fintCacheManager;
    }

    @Bean
    FintCache<String, AdministrativEnhetResource> administrativEnhetResourceCache() {
        return createCache(AdministrativEnhetResource.class);
    }

    @Bean
    FintCache<String, ArkivdelResource> arkivdelResourceCache() {
        return createCache(ArkivdelResource.class);
    }

    @Bean
    FintCache<String, ArkivressursResource> arkivressursResourceCache() {
        return createCache(ArkivressursResource.class);
    }

    @Bean
    FintCache<String, DokumentStatusResource> dokumentStatusResourceCache() {
        return createCache(DokumentStatusResource.class);
    }

    @Bean
    FintCache<String, DokumentTypeResource> dokumentTypeResourceCache() {
        return createCache(DokumentTypeResource.class);
    }

    @Bean
    FintCache<String, KlassifikasjonssystemResource> klassifikasjonssystemResourceCache() {
        return createCache(KlassifikasjonssystemResource.class);
    }

    @Bean
    FintCache<String, KlasseResource> klasseResourceCache() {
        return createCache(KlasseResource.class);
    }

    @Bean
    FintCache<String, RolleResource> rolleResourceCache() {
        return createCache(RolleResource.class);
    }

    @Bean
    FintCache<String, PartRolleResource> partRolleResourceCache() {
        return createCache(PartRolleResource.class);
    }

    @Bean
    FintCache<String, KorrespondansepartTypeResource> korrespondansepartTypeResourceCache() {
        return createCache(KorrespondansepartTypeResource.class);
    }

    @Bean
    FintCache<String, SaksstatusResource> saksstatusResourceCache() {
        return createCache(SaksstatusResource.class);
    }

    @Bean
    FintCache<String, SkjermingshjemmelResource> skjermingshjemmelResourceCache() {
        return createCache(SkjermingshjemmelResource.class);
    }

    @Bean
    FintCache<String, TilgangsrestriksjonResource> tilgangsrestriksjonResourceCache() {
        return createCache(TilgangsrestriksjonResource.class);
    }

    @Bean
    FintCache<String, JournalStatusResource> journalstatusResourceCache() {
        return createCache(JournalStatusResource.class);
    }

    @Bean
    FintCache<String, JournalpostTypeResource> journalposttypeResourceCache() {
        return createCache(JournalpostTypeResource.class);
    }

    @Bean
    FintCache<String, SaksmappetypeResource> saksmappetypeResourceCache() {
        return createCache(SaksmappetypeResource.class);
    }

    @Bean
    FintCache<String, VariantformatResource> variantformatResourceCache() {
        return createCache(VariantformatResource.class);
    }

    @Bean
    FintCache<String, FormatResource> formatResourceCache() {
        return createCache(FormatResource.class);
    }

    @Bean
    FintCache<String, PersonalressursResource> personalressursResourceCache() {
        return createCache(PersonalressursResource.class);
    }

    @Bean
    FintCache<String, PersonResource> personResourceCache() {
        return createCache(PersonResource.class);
    }

    private <V> FintCache<String, V> createCache(Class<V> resourceClass) {
        return fintCacheManager.createCache(
                resourceClass.getName().toLowerCase(Locale.ROOT),
                String.class,
                resourceClass
        );
    }

}
