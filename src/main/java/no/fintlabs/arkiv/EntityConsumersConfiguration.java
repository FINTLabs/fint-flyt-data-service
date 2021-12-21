package no.fintlabs.arkiv;//package no.fintlabs.integration;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.kafka.consumer.EntityConsumerFactory;
import no.fintlabs.kafka.topic.DomainContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class EntityConsumersConfiguration {

    @Bean
    ConcurrentMessageListenerContainer<String, String> administrativEnhetResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.noark.administrativenhet",
                AdministrativEnhetResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> arkivdelResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.noark.arkivdel",
                ArkivdelResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> arkivressursResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.noark.arkivressurs",
                ArkivressursResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> dokumentStatusResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.kodeverk.dokumentstatus",
                DokumentStatusResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> dokumentTypeResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.kodeverk.dokumenttype",
                DokumentTypeResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

//    @Bean
//    ConcurrentMessageListenerContainer<String, String> klasseResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
//        return entityConsumerFactory.createEntityConsumer(
//                DomainContext.SKJEMA,
//                "arkiv.noark.klasse",
//                KlasseResource.class,
//                resource -> Stream.concat(
//                        Stream.of(resource.getKlasseId()),
//                        resource.getSelfLinks().stream().map(Link::getHref)
//                ).collect(Collectors.toList()),
//                true
//        );
//    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> klassifikasjonssystemResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.noark.klassifikasjonssystem",
                KlassifikasjonssystemResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> klassifikasjonstypeResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.kodeverk.klassifikasjonstype",
                KlassifikasjonstypeResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> rolleResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.kodeverk.rolle",
                RolleResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> saksstatusResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.kodeverk.saksstatus",
                SaksstatusResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> skjermingshjemmelResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.kodeverk.skjermingshjemmel",
                SkjermingshjemmelResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> tilgangsrestriksjonResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "arkiv.kodeverk.tilgangsrestriksjon",
                TilgangsrestriksjonResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getSystemId().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> personalressursResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "administrasjon.personal.personalressurs",
                PersonalressursResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getAnsattnummer().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, String> personResourceEntityConsumer(EntityConsumerFactory entityConsumerFactory) {
        return entityConsumerFactory.createEntityConsumer(
                DomainContext.SKJEMA,
                "administrasjon.personal.person",
                PersonResource.class,
                resource -> Stream.concat(
                        Stream.of(resource.getFodselsnummer().getIdentifikatorverdi()),
                        resource.getSelfLinks().stream().map(Link::getHref)
                ).collect(Collectors.toList()),
                true
        );
    }

}
