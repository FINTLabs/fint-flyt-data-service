package no.fintlabs.arkiv;

import no.fint.model.resource.FintLinks;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import no.fintlabs.links.ResourceLinkUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class ResourceEntityConsumersConfiguration {

    private final EntityConsumerFactoryService entityConsumerFactoryService;


    public ResourceEntityConsumersConfiguration(EntityConsumerFactoryService entityConsumerFactoryService) {
        this.entityConsumerFactoryService = entityConsumerFactoryService;
    }

    private <T extends FintLinks> ConcurrentMessageListenerContainer<String, T> createCacheConsumer(
            String resourceReference,
            Class<T> resourceClass,
            FintCache<String, T> cache
    ) {
        return entityConsumerFactoryService.createFactory(
                resourceClass,
                consumerRecord -> cache.put(
                        ResourceLinkUtil.getSelfLinks(consumerRecord.value()),
                        consumerRecord.value()
                ),
                new CommonLoggingErrorHandler()
        ).createContainer(EntityTopicNameParameters.builder().resource(resourceReference).build());
    }

    @Bean
    ConcurrentMessageListenerContainer<String, AdministrativEnhetResource> administrativEnhetResourceEntityConsumer(
            FintCache<String, AdministrativEnhetResource> administrativEnhetResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.noark.administrativenhet",
                AdministrativEnhetResource.class,
                administrativEnhetResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, ArkivdelResource> arkivdelResourceEntityConsumer(
            FintCache<String, ArkivdelResource> arkivdelResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.noark.arkivdel",
                ArkivdelResource.class,
                arkivdelResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, ArkivressursResource> arkivressursResourceEntityConsumer(
            FintCache<String, ArkivressursResource> arkivressursResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.noark.arkivressurs",
                ArkivressursResource.class,
                arkivressursResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, TilknyttetRegistreringSomResource> tilknyttetRegistreringSomResourceEntityConsumer(
            FintCache<String, TilknyttetRegistreringSomResource> tilknyttetRegistreringSomResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.tilknyttetregistreringsom",
                TilknyttetRegistreringSomResource.class,
                tilknyttetRegistreringSomResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, DokumentStatusResource> dokumentStatusResourceEntityConsumer(
            FintCache<String, DokumentStatusResource> dokumentStatusResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.dokumentstatus",
                DokumentStatusResource.class,
                dokumentStatusResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, DokumentTypeResource> dokumentTypeResourceEntityConsumer(
            FintCache<String, DokumentTypeResource> dokumentTypeResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.dokumenttype",
                DokumentTypeResource.class,
                dokumentTypeResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, KlassifikasjonssystemResource> klassifikasjonssystemResourceEntityConsumer(
            FintCache<String, KlassifikasjonssystemResource> klassifikasjonssystemResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.noark.klassifikasjonssystem",
                KlassifikasjonssystemResource.class,
                klassifikasjonssystemResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, RolleResource> rolleResourceEntityConsumer(
            FintCache<String, RolleResource> rolleResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.rolle",
                RolleResource.class,
                rolleResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, PartRolleResource> partRolleResourceEntityConsumer(
            FintCache<String, PartRolleResource> partRolleResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.partrolle",
                PartRolleResource.class,
                partRolleResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, KorrespondansepartTypeResource> korrespondansepartTypeResourceEntityConsumer(
            FintCache<String, KorrespondansepartTypeResource> korrespondansepartTypeResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.korrespondanseparttype",
                KorrespondansepartTypeResource.class,
                korrespondansepartTypeResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, SaksstatusResource> saksstatusResourceEntityConsumer(
            FintCache<String, SaksstatusResource> saksstatusResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.saksstatus",
                SaksstatusResource.class,
                saksstatusResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, SkjermingshjemmelResource> skjermingshjemmelResourceEntityConsumer(
            FintCache<String, SkjermingshjemmelResource> skjermingshjemmelResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.skjermingshjemmel",
                SkjermingshjemmelResource.class,
                skjermingshjemmelResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, TilgangsrestriksjonResource> tilgangsrestriksjonResourceEntityConsumer(
            FintCache<String, TilgangsrestriksjonResource> tilgangsrestriksjonResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.tilgangsrestriksjon",
                TilgangsrestriksjonResource.class,
                tilgangsrestriksjonResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, JournalStatusResource> journalstatusResourceEntityConsumer(
            FintCache<String, JournalStatusResource> journalStatusResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.journalstatus",
                JournalStatusResource.class,
                journalStatusResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, JournalpostTypeResource> journalposttypeEntityConsumer(
            FintCache<String, JournalpostTypeResource> journalpostTypeResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.journalposttype",
                JournalpostTypeResource.class,
                journalpostTypeResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, SaksmappetypeResource> saksmappetypeEntityConsumer(
            FintCache<String, SaksmappetypeResource> saksmappeTypeResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.saksmappetype",
                SaksmappetypeResource.class,
                saksmappeTypeResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, VariantformatResource> variantformatResourceEntityConsumer(
            FintCache<String, VariantformatResource> variantformatResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.variantformat",
                VariantformatResource.class,
                variantformatResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, FormatResource> formatResourceEntityConsumer(
            FintCache<String, FormatResource> formatResourceCache
    ) {
        return createCacheConsumer(
                "arkiv.kodeverk.format",
                FormatResource.class,
                formatResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, PersonalressursResource> personalressursResourceEntityConsumer(
            FintCache<String, PersonalressursResource> personalressursResourceCache
    ) {
        return createCacheConsumer(
                "administrasjon.personal.personalressurs",
                PersonalressursResource.class,
                personalressursResourceCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, PersonResource> personResourceEntityConsumer(
            FintCache<String, PersonResource> personResourceCache
    ) {
        return createCacheConsumer(
                "administrasjon.personal.person",
                PersonResource.class,
                personResourceCache
        );
    }

}
